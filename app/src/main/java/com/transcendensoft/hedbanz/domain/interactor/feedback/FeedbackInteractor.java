package com.transcendensoft.hedbanz.domain.interactor.feedback;

import android.os.Build;
import android.text.TextUtils;

import com.transcendensoft.hedbanz.data.repository.FeedbackRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Feedback;
import com.transcendensoft.hedbanz.domain.interactor.feedback.exception.FeedbackException;
import com.transcendensoft.hedbanz.domain.repository.FeedbackRepository;
import com.transcendensoft.hedbanz.domain.validation.FeedbackError;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for submitting feedback
 * of some specific user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FeedbackInteractor extends CompletableUseCase<Feedback>{
    private FeedbackRepository feedbackRepository;
    private FeedbackException feedbackException;

    @Inject
    public FeedbackInteractor(CompletableTransformer completableTransformer,
                              CompositeDisposable mCompositeDisposable,
                              FeedbackRepository feedbackRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.feedbackRepository = feedbackRepository;
        feedbackException = new FeedbackException();
    }

    @Override
    protected Completable buildUseCaseCompletable(Feedback feedback) {
        if(TextUtils.isEmpty(feedback.getFeedbackText())){
            feedbackException.addError(FeedbackError.EMPTY_FEEDBACK);
            return Completable.error(feedbackException);
        }
        return feedbackRepository.submitFeedback(feedback);
    }
}
