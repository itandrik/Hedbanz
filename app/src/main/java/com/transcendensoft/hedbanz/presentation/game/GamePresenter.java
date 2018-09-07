package com.transcendensoft.hedbanz.presentation.game;
/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.PlayerGuessing;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.interactor.game.GameInteractorFacade;
import com.transcendensoft.hedbanz.domain.interactor.game.GetMessagesInteractor;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.validation.RoomError;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
import com.transcendensoft.hedbanz.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Implementation of game mode presenter.
 * Here are work with server by sockets and other like
 * processing game algorithm.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class GamePresenter extends BasePresenter<Room, GameContract.View>
        implements GameContract.Presenter, RecyclerDelegationAdapter.OnRecyclerBorderListener {
    private GameInteractorFacade mGameInteractor;
    private GetMessagesInteractor mGetMessagesInteractor;
    private PreferenceManager mPreferenceManger;
    private List<User> mTypingUsers;
    private boolean isAfterRoomCreation;
    private boolean isLeaveFromRoom = false;
    private boolean isUserKicked = false;
    private boolean isSocketInititalized = false;

    @Inject
    public GamePresenter(GameInteractorFacade gameInteractor,
                         GetMessagesInteractor getMessagesInteractor,
                         PreferenceManager preferenceManager) {
        this.mGameInteractor = gameInteractor;
        this.mGetMessagesInteractor = getMessagesInteractor;
        this.mPreferenceManger = preferenceManager;

        mTypingUsers = new ArrayList<>();
    }

    @Override
    public void setIsLeaveFromRoom(boolean isLeaveFromRoom) {
        this.isLeaveFromRoom = isLeaveFromRoom;
    }

    /*------------------------------------*
     *---------- Base presenter ----------*
     *------------------------------------*/
    @Override
    protected void updateView() {
        if (model.getMessages() == null || model.getMessages().isEmpty()) {
            model.setMessages(new ArrayList<>());
            view().showLoading();
            if (!isSocketInititalized) {
                initSockets();
                isSocketInititalized = true;
            }
        } else {
            view().clearMessages();
            view().addMessages(model.getMessages());
        }
    }

    @Override
    public void destroy() {
        mGetMessagesInteractor.dispose();
        mGameInteractor.destroy();
    }

    @Override
    public void bindView(@NonNull GameContract.View view) {
        super.bindView(view);
        if (mGameInteractor != null) {
            mGameInteractor.resumeSocket();
        }
        if (mPreferenceManger.isUserKicked()) {
            view.showUserKicked();
            return;
        }
        if (mPreferenceManger.isLastUser()) {
            view.showLastUserDialog();
        }
        mPreferenceManger.setIsGameEnabled(true);

        if (mGameInteractor.doesGameHasServerConnectionError()) {
            view().showLoadingDialog();
        }
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (mGameInteractor != null && !isLeaveFromRoom) {
            mGameInteractor.stopSocket();
        }
        mPreferenceManger.setIsGameEnabled(false);
    }

    @Override
    public void setAfterRoomCreation(boolean afterRoomCreation) {
        isAfterRoomCreation = afterRoomCreation;
    }

    @Override
    public boolean doesGameHasServerConnectionError() {
        return mGameInteractor.doesGameHasServerConnectionError();
    }

    @Override
    public void leaveFromRoom() {
        mGameInteractor.leaveFromRoom();
    }

    /*------------------------------------*
     *--------- Messages loading ---------*
     *------------------------------------*/
    @Override
    public void onBottomReached() {
        Timber.i("BOTTOM reached");
    }

    @Override
    public void onTopReached() {
        Timber.i("TOP reached");
        GetMessagesInteractor.Param param = new GetMessagesInteractor.Param(
                model.getId(), model.getPlayers().size());
        mGetMessagesInteractor.loadNextPage()
                .execute(new MessageListObserver(view(), model), param);
    }

    private void refreshMessageHistory() {
        GetMessagesInteractor.Param param = new GetMessagesInteractor.Param(
                model.getId(), model.getPlayers().size());
        mGetMessagesInteractor.refresh(null)
                .execute(new MessageListObserver(view(), model), param);
    }

    /*------------------------------------*
     *----- Recycler click listeners -----*
     *------------------------------------*/
    @Override
    public void processRetryNetworkPagination(Observable<Object> clickObservable) {
        processRetryServerPagination(clickObservable);
    }

    @Override
    public void processRetryServerPagination(Observable<Object> clickObservable) {
        if (clickObservable != null) {
            addDisposable(clickObservable.subscribe(
                    obj -> onTopReached(),
                    err -> Timber.e("Retry network in pagination error")
            ));
        }
    }

    @Override
    public void processSetWordToUserObservable(Observable<Word> sendWordObservable) {
        if (sendWordObservable != null) {
            addDisposable(sendWordObservable.subscribe(
                    word -> {
                        word.setUserFrom(mPreferenceManger.getUser());
                        updateSettingWordViewParameters(word.getUserFrom(), false, true);
                        mGameInteractor.setWordToUser(word);
                    },
                    err -> Timber.e("Error while send word to user. Message : " + err.getMessage())
            ));
        }
    }

    private void updateSettingWordViewParameters(User senderUser, boolean isFinished, boolean isLoading) {
        for (int i = model.getMessages().size() - 1; i >= 0; i--) {
            Message message = model.getMessages().get(i);
            if (message instanceof Word && message.getMessageType() == MessageType.WORD_SETTING &&
                    mPreferenceManger.getUser().equals(senderUser)) {
                Word settingWord = (Word) message;
                settingWord.setFinished(isFinished);
                settingWord.setLoading(isLoading);
                view().setMessage(i, settingWord);
                break;
            }
        }
    }

    @Override
    public void processGuessWordHelperText(Observable<Question> clickObservable) {
        processGuessWordSubmit(clickObservable);
    }

    @Override
    public void processGuessWordSubmit(Observable<Question> clickObservable) {
        addDisposable(clickObservable
                .distinct(Question::getQuestionId)
                .subscribe(
                        questionFromView -> {
                            Question question = mGameInteractor.guessWord(questionFromView.getQuestionId(),
                                    questionFromView.getMessage());

                            question.setMessageType(MessageType.ASKING_QUESTION_THIS_USER);
                            question.setMessage(questionFromView.getMessage());
                            question.setAllUsersCount(model.getPlayers().size() - 1);

                            updateSettingQuestionViewParameters(question.getUserFrom(),
                                    false, true, questionFromView.getMessage());

                            model.getMessages().add(question);
                            view().addMessage(question);

                            disposeAll();
                        },
                        err -> Timber.e("Error while guess word. Message : " + err.getMessage())
                ));
    }

    private void updateSettingQuestionViewParameters(User senderUser, boolean isFinished,
                                                     boolean isLoading, String messageText) {
        for (int i = model.getMessages().size() - 1; i >= 0; i--) {
            Message message = model.getMessages().get(i);
            if (message.getMessageType() == MessageType.GUESS_WORD_THIS_USER &&
                    mPreferenceManger.getUser().equals(senderUser)) {
                message.setMessage(messageText);
                message.setFinished(isFinished);
                message.setLoading(isLoading);
                view().setMessage(i, message);
                break;
            }
        }
    }

    @Override
    public void processThumbsDownClick(Observable<Long> clickObservable) {
        addDisposable(clickObservable.subscribe(
                questionId -> mGameInteractor.voteForQuestion(Question.Vote.NO, questionId),
                err -> Timber.e("Error while vote \'no\'. Message : " + err.getMessage())
        ));
    }

    @Override
    public void processThumbsUpClick(Observable<Long> clickObservable) {
        addDisposable(clickObservable.subscribe(
                questionId -> mGameInteractor.voteForQuestion(Question.Vote.YES, questionId),
                err -> Timber.e("Error while vote \'yes\'. Message : " + err.getMessage())
        ));
    }

    @Override
    public void processWinClick(Observable<Long> clickObservable) {
        addDisposable(clickObservable.subscribe(
                questionId -> mGameInteractor.voteForQuestion(Question.Vote.WIN, questionId),
                err -> Timber.e("Error while vote \'win\'. Message : " + err.getMessage())
        ));
    }

    @Override
    public void processRestartGameClick(Observable<View> clickObservable) {
        addDisposable(clickObservable
                .subscribe(view -> {
                            view().showLoadingDialog();
                            mGameInteractor.restartGame();
                        },
                        err -> Timber.e("Error while restart game click." +
                                " Message : " + err.getMessage())));

    }

    @Override
    public void processCancelGameClick(Observable<View> clickObservable) {
        addDisposable(clickObservable
                .subscribe(view -> view().onBackPressed(),
                        err -> Timber.e("Error while cancel game click." +
                                " Message : " + err.getMessage())));
    }

    /*------------------------------------*
     *------ Socket initialization -------*
     *------------------------------------*/
    @Override
    public void initSockets() {
        initSocketSystemListeners();
        initBusinessLogicListeners();

        mGameInteractor.connectSocketToServer(model.getId());
    }

    private void initSocketSystemListeners() {
        mGameInteractor.onConnectListener(
                str -> {
                    Timber.i("Socket connected: %s", str);

                    if (view() != null) {
                        view().hideLoadingDialog();
                        if (!isAfterRoomCreation) {
                            if (mPreferenceManger.getCurrentRoomId() == -1) {
                                mGameInteractor.joinToRoom(model.getPassword());
                            } else {
                                view().showRestoreRoom();
                            }
                        } else {
                            mGameInteractor.sendConnectInfo();
                            mGameInteractor.setRoomInfo(model);
                            initRoom(model);
                        }
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onDisconnectListener(
                str -> {
                    Timber.i("Socket disconnected!");
                    if (view() != null) {
                        view().showFooterDisconnected();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onConnectErrorListener(
                str -> {
                    Timber.e("Socket connect ERROR: %s", str);
                    if (view() != null) {
                        view().showFooterDisconnected();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onConnectTimeoutListener(
                str -> {
                    Timber.e("Socket connect TIMEOUT: %s", str);
                    if (view() != null) {
                        view().showFooterDisconnected();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectListener(
                str -> {
                    Timber.i("Socket reconnected!");
                    if (view() != null) {
                        view().showFooterReconnected();
                        view().hideLoadingDialog();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectErrorListener(str -> {
                    Timber.e("Socket reconnect error %s", str);
                    if (view() != null) {
                        view().showFooterDisconnected();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectingListener(str -> {
                    Timber.i("Socket reconnecting... %s", str);
                    if (view() != null) {
                        view().showFooterReconnecting();
                    }
                },
                this::processEventListenerOnError);
    }

    private void initBusinessLogicListeners() {
        mGameInteractor.onRoomInfoListener(
                this::initRoom,
                this::processEventListenerOnError);
        mGameInteractor.onRoomRestoredListener(
                this::initRoom,
                this::processEventListenerOnError);
        mGameInteractor.onErrorListener(
                this::processRoomError,
                this::processEventListenerOnError);
    }

    private void initRoom(Room room) {
        model = room;
        model.setMessages(new ArrayList<>());

        initJoinedUserListener();
        initLeftUserListener();
        initUserAfkListener();
        initUserReturnedListener();
        initPlayersInfoListener();
        initUserWinListener();
        initMessageListeners();
        initWordSettingListeners();
        initWordGuessingListeners();
        initAskingQuestionListener();
        initWordVotingListeners();
        initUserKickListeners();
        initGameOverListener();
        initWaitingForUsersListener();

        refreshMessageHistory();
    }

    private void initLeftUserListener() {
        mGameInteractor.onLeftUserListener(
                this::processLeftUserOnNext,
                this::processEventListenerOnError);
    }

    private void initJoinedUserListener() {
        mGameInteractor.onJoinedUserListener(
                user -> {
                    List<User> users = model.getPlayers();
                    if (!users.contains(user)) {
                        users.add(user);
                    }
                    if (!mPreferenceManger.getUser().equals(user)) {
                        Message message = new Message.Builder()
                                .setUserFrom(user)
                                .setMessageType(MessageType.JOINED_USER)
                                .build();
                        model.getMessages().add(message);
                        view().addMessage(message);
                    }
                },
                this::processEventListenerOnError);
    }

    private void initUserAfkListener() {
        mGameInteractor.onUserAfkListener(user -> {
            //view().showUserAfk(true, user.getLogin());
        }, this::processEventListenerOnError);
    }

    private void initUserReturnedListener() {
        mGameInteractor.onUserReturnedListener(user -> {
            if (!mPreferenceManger.getUser().equals(user)) {
                for (int i = model.getMessages().size() - 1; i >= 0; i--) {
                    Message message = model.getMessages().get(i);

                    if ((message.getMessageType() == MessageType.USER_KICK_WARNING) &&
                            message.getUserFrom().equals(user)) {
                        model.getMessages().remove(i);
                        view().removeMessage(i);
                        break;
                    }
                }
            } else {
                // Here was history loading
            }
        }, this::processEventListenerOnError);
    }

    private void initPlayersInfoListener() {
        mGameInteractor.onPlayersInfoUseCase(users -> {
            if(view() != null) {
                view().hideLoadingDialog();
                model.setPlayers(users);
                model.setMessages(new ArrayList<>());
                view().clearMessages();
                view().showLoading();
                refreshMessageHistory();
            }
        }, this::processEventListenerOnError);
    }

    private void initUserWinListener() {
        mGameInteractor.onUserWinListener(user -> {

            List<Message> messages = model.getMessages();
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message modelMessage = messages.get(i);
                if (modelMessage instanceof Question) {
                    Question modelQuestion = (Question) modelMessage;
                    int winVotersCount = modelQuestion.getWinVoters().size();
                    int allUsersCount = modelQuestion.getAllUsersCount() != null ?
                            modelQuestion.getAllUsersCount() : 0;
                    if (winVotersCount / allUsersCount > 0.8) {
                        modelQuestion.setWin(true);
                        view().setMessage(i, modelQuestion);
                    }
                    break;
                }
            }

            Message winMessage = new Message.Builder()
                    .setUserFrom(user)
                    .build();

            if (mPreferenceManger.getUser().equals(user)) {
                view().showWinDialog();
                winMessage.setMessageType(MessageType.USER_WINS_THIS);
            } else {
                winMessage.setMessageType(MessageType.USER_WINS_OTHER);
            }

            Message guessWordMessage = null;
            int guessWordMessagePosition = 0;
            for (int i = model.getMessages().size() - 1; i >= 0; i--) {
                Message message = model.getMessages().get(i);
                if (message.getMessageType().equals(MessageType.GUESS_WORD_THIS_USER) &&
                        message.getUserFrom().equals(mPreferenceManger.getUser())) {
                    guessWordMessage = message;
                    guessWordMessagePosition = i;
                }
                if (message.getMessageType().equals(MessageType.ASKING_QUESTION_THIS_USER)) {
                    if (guessWordMessage != null) {
                        model.getMessages().remove(guessWordMessage);
                        view().removeMessage(guessWordMessagePosition);
                    }
                    break;
                }
            }

            messages.add(winMessage);
            view().addMessage(winMessage);
        }, this::processEventListenerOnError);
    }

    private void initWordSettingListeners() {

        mGameInteractor.onWordSettedListener(word -> {
            word.setMessageType(MessageType.WORD_SETTED);
            model.getMessages().add(word);
            view().addMessage(word);

            updateSettingWordViewParameters(word.getUserFrom(), true, false);
        }, this::processEventListenerOnError);

        mGameInteractor.onWordSettingListener(wordReceiverUser -> {
            view().hideLoadingDialog();

            Word word = new Word.Builder()
                    .setMessageType(MessageType.WORD_SETTING)
                    .setWordReceiverUser(wordReceiverUser)
                    .build();

            model.getMessages().add(word);
            view().addMessage(word);

            setGameOverViewAsFinished();
        }, this::processEventListenerOnError);
    }

    private void setGameOverViewAsFinished() {
        List<Message> messages = model.getMessages();
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            if (message.getMessageType().equals(MessageType.GAME_OVER)) {
                message.setLoading(false);
                message.setFinished(true);
                view().setMessage(i, message);
                break;
            }
        }
    }

    private void initMessageListeners() {
        mGameInteractor.onStartTypingListener(
                this::processStartTyping,
                this::processEventListenerOnError);
        mGameInteractor.onStopTypingListener(
                this::processStopTyping,
                this::processEventListenerOnError);
        mGameInteractor.onMessageReceivedListener(
                this::processSimpleMessage,
                this::processEventListenerOnError);
    }

    private void initWordGuessingListeners() {
        mGameInteractor.onWordGuessingListener(
                this::processGuessWord,
                this::processEventListenerOnError
        );
    }

    private void initAskingQuestionListener() {
        mGameInteractor.onQuestionAskingListener(
                this::processAskingQuestion,
                this::processEventListenerOnError
        );
    }

    private void initWordVotingListeners() {
        mGameInteractor.onQuestionVotingListener(
                this::processVoting,
                this::processEventListenerOnError
        );
    }

    private void initUserKickListeners() {
        mGameInteractor.onKickWarningListener(
                this::processUserKickWarning,
                this::processEventListenerOnError
        );
        mGameInteractor.onUserKickedListener(
                this::processUserKicked,
                this::processEventListenerOnError);
    }

    private void initGameOverListener() {
        mGameInteractor.onGameOverListener(
                this::processGameOverEvent,
                this::processEventListenerOnError
        );
    }

    private void initWaitingForUsersListener() {
        mGameInteractor.onWaitingForUsersListener(
                this::processWaitingForUsersEvent,
                this::processEventListenerOnError
        );
    }

    private void processLeftUserOnNext(User user) {
        List<User> users = model.getPlayers();
        if (users.contains(user)) {
            users.remove(user);
        }
        Message message = new Message.Builder()
                .setUserFrom(user)
                .setMessageType(MessageType.LEFT_USER)
                .build();
        model.getMessages().add(message);
        view().addMessage(message);

        boolean containsGuess = false;
        for (int i = model.getMessages().size() - 1; i >= 0; i--) {
            Message message1 = model.getMessages().get(i);
            if (message1.getMessageType().equals(MessageType.GUESS_WORD_THIS_USER) ||
                    message1.getMessageType().equals(MessageType.GUESS_WORD_OTHER_USER)) {
                containsGuess = true;
                break;
            }
            if (message1.getMessageType().equals(MessageType.WORD_SETTING)) {
                break;
            }
        }
        if (!containsGuess) {
            mGameInteractor.decRoomMaxPlayers();
        }
    }

    private void processRoomError(RoomError roomError) {
        Timber.e("Room error from server. Code: " + roomError.getErrorCode());
        switch (roomError) {
            case SUCH_PLAYER_ALREADY_IN_ROOM:
                mGameInteractor.restoreRoom();
                break;
            case UNAUTHORIZED_PLAYER:
            case INCORRECT_PASSWORD:
            case INVALID_PASSWORD:
            case NO_SUCH_ROOM:
            case ROOM_IS_FULL:
            case CANT_START_GAME:
            case GAME_HAS_BEEN_ALREADY_STARTED:
            case USER_HAS_MAX_ACTIVE_ROOMS_NUMBER:
                view().showErrorDialog(roomError.getErrorMessage());
                break;
            default:
                view().showErrorToast(roomError.getErrorMessage());

        }
    }

    private void processSimpleMessage(Message message) {
        List<Message> messages = model.getMessages();

        if (message.getMessageType() == MessageType.SIMPLE_MESSAGE_THIS_USER &&
                !message.isLoading() && message.isFinished()) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message modelMessage = messages.get(i);
                modelMessage.setLoading(false);
                modelMessage.setFinished(true);
                modelMessage.setCreateDate(message.getCreateDate());
                if (modelMessage.getClientMessageId().equals(message.getClientMessageId())) {
                    view().setMessage(i, modelMessage);
                    break;
                }
            }
        } else {
            messages.add(message);
            view().addMessage(message);
        }
    }

    private void processStartTyping(User user) {
        if (!mTypingUsers.contains(user)) {
            mTypingUsers.add(user);
        }
        view().showFooterTyping(mTypingUsers);
    }

    private void processStopTyping(User user) {
        if (mTypingUsers.contains(user)) {
            mTypingUsers.remove(user);
        }
        view().showFooterTyping(mTypingUsers);
    }

    private void processEventListenerOnError(Throwable err) {
        if (err instanceof IncorrectJsonException) {
            IncorrectJsonException incorrectJsonException = (IncorrectJsonException) err;
            Timber.e("Incorrect JSON from socket listener. JSON: %S; EVENT: %s",
                    incorrectJsonException.getJson(), incorrectJsonException.getMethod());
        }
    }

    private void processGuessWord(PlayerGuessing playerGuessing) {
        User currentUser = mPreferenceManger.getUser();

        playerGuessing.setUserFrom(playerGuessing.getPlayer());
        if (playerGuessing.getPlayer() != null) {
            playerGuessing.setClientMessageId(playerGuessing.getPlayer().getId());

            if (currentUser.equals(playerGuessing.getPlayer())) {
                playerGuessing.setMessageType(MessageType.GUESS_WORD_THIS_USER);
            } else {
                playerGuessing.setMessageType(MessageType.GUESS_WORD_OTHER_USER);
            }
        }

        model.getMessages().add(playerGuessing);
        view().addMessage(playerGuessing);
    }

    private void processAskingQuestion(Question question) {
        User currentUser = mPreferenceManger.getUser();
       /* Message lastMessage = model.getMessages().get(model.getMessages().size()-1);
        if(lastMessage instanceof Question &&
                lastMessage.getMessage().equalsIgnoreCase(question.getMessage())){
            return;
        }//TODO*/

        if (currentUser.equals(question.getUserFrom()) &&
                !question.isLoading() && question.isFinished()) {
            question.setMessageType(MessageType.ASKING_QUESTION_THIS_USER);

            updateSettingQuestionViewParameters(
                    question.getUserFrom(), true, false, question.getMessage());

            List<Message> messages = model.getMessages();
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message modelMessage = messages.get(i);
                modelMessage.setLoading(false);
                modelMessage.setFinished(true);
                modelMessage.setCreateDate(question.getCreateDate());
                if (modelMessage instanceof Question &&
                        modelMessage.getClientMessageId().equals(question.getClientMessageId())) {
                    Question modelQuestion = (Question) modelMessage;
                    modelQuestion.setQuestionId(question.getQuestionId());
                    modelQuestion.setNoVoters(question.getNoVoters());
                    modelQuestion.setYesVoters(question.getYesVoters());
                    modelQuestion.setAllUsersCount((byte) model.getPlayers().size() - 1);
                    view().setMessage(i, modelQuestion);
                    break;
                }
            }
        } else {
            question.setMessageType(MessageType.ASKING_QUESTION_OTHER_USER);
            question.setAllUsersCount((byte) model.getPlayers().size() - 1);
            model.getMessages().add(question);
            view().addMessage(question);
        }
    }

    private void processVoting(Question question) {
        for (int i = model.getMessages().size() - 1; i >= 0; i--) {
            Message modelMessage = model.getMessages().get(i);
            if (modelMessage instanceof Question &&
                    ((Question) modelMessage).getQuestionId() == question.getQuestionId()) {
                Question modelQuestion = (Question) modelMessage;
                modelQuestion.setYesVoters(question.getYesVoters());
                modelQuestion.setNoVoters(question.getNoVoters());
                modelQuestion.setWinVoters(question.getWinVoters());

                view().setMessage(i, modelQuestion);
                return;
            }
        }
    }

    private void processUserKickWarning(User user) {
        Message message = new Message.Builder()
                .setUserFrom(user)
                .setMessageType(MessageType.USER_KICK_WARNING)
                .build();
        model.getMessages().add(message);
        view().addMessage(message);
    }

    private void processUserKicked(User user) {
        List<User> users = model.getPlayers();
        if (users.contains(user)) {
            users.remove(user);
        }
        Message message = new Message.Builder()
                .setUserFrom(user)
                .setMessageType(MessageType.USER_KICKED)
                .build();
        model.getMessages().add(message);
        view().addMessage(message);
    }

    private void processGameOverEvent(Boolean stub) {
        Message message = new Message.Builder()
                .setMessageType(MessageType.GAME_OVER)
                .build();
        message.setLoading(false);
        message.setFinished(false);
        model.getMessages().add(message);
        view().addMessage(message);
    }

    private void processWaitingForUsersEvent(Boolean stub) {
        view().hideLoadingDialog();
        setGameOverViewAsFinished();

        Message message = new Message.Builder()
                .setMessageType(MessageType.WAITING_FOR_USERS)
                .build();
        model.getMessages().add(message);
        view().addMessage(message);
    }

    /*------------------------------------*
     *---- Send socket event messages ----*
     *------------------------------------*/
    @Override
    public void messageTextChanges(EditText editText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .skip(1)
                        .filter(text -> !TextUtils.isEmpty(text))
                        .compose(RxUtils.debounceFirst(500, TimeUnit.MILLISECONDS))
                        .doOnNext((str) -> {
                            mGameInteractor.startTyping();
                        })
                        .mergeWith(RxTextView.textChanges(editText)
                                .skip(1)
                                .debounce(500, TimeUnit.MILLISECONDS)
                                .doOnNext(str -> {
                                    mGameInteractor.stopTyping();
                                }))
                        .subscribe());
    }

    @Override
    public void sendMessage(String text) {
        Message message = mGameInteractor.sendMessage(text);

        model.getMessages().add(message);
        view().addMessage(message);
    }

    @Override
    public void restoreRoom() {
        mGameInteractor.restoreRoom();
    }
}
