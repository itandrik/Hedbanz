package com.transcendensoft.hedbanz.domain.interactor.feedback;

import android.os.Build;
import android.text.TextUtils;

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.repository.FeedbackRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Feedback;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.interactor.feedback.exception.FeedbackException;
import com.transcendensoft.hedbanz.domain.repository.FeedbackRepository;
import com.transcendensoft.hedbanz.domain.validation.FeedbackError;
import com.transcendensoft.hedbanz.domain.validation.RoomError;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for submitting feedback
 * of some specific user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FeedbackInteractor extends ObservableUseCase<Boolean, Feedback> {
    private FeedbackRepository feedbackRepository;
    private FeedbackException feedbackException;

    @Inject
    public FeedbackInteractor(ObservableTransformer completableTransformer,
                              CompositeDisposable mCompositeDisposable,
                              FeedbackRepository feedbackRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.feedbackRepository = feedbackRepository;
        feedbackException = new FeedbackException();
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Feedback feedback) {
        if(TextUtils.isEmpty(feedback.getFeedbackText())){
            feedbackException.addError(FeedbackError.EMPTY_FEEDBACK);
            return Observable.error(feedbackException);
        }
        return feedbackRepository.submitFeedback(feedback)
                .onErrorResumeNext(this::processFeedbackError);
    }

    private Observable<Boolean> processFeedbackError(Throwable throwable) {
        if(throwable instanceof HedbanzApiException){
            HedbanzApiException exception = (HedbanzApiException) throwable;
            feedbackException.addError(
                    FeedbackError.Companion.getErrorByCode(exception.getServerErrorCode()));
        } else {
            feedbackException.addError(FeedbackError.UNDEFINED_ERROR);
        }
        return Observable.error(feedbackException);
    }
}
