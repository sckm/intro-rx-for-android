package com.github.scache.introrxforandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.scache.introrxforandroid.api.GithubService;
import com.github.scache.introrxforandroid.api.RetrofitManager;
import com.github.scache.introrxforandroid.model.GithubUser;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.Random;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    Button mRefreshButton;
    View mUserView1;
    View mUserView2;
    View mUserView3;

    Observable<Void> refreshClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRefreshButton = (Button) findViewById(R.id.btn_refresh);
        mUserView1 = findViewById(R.id.user1);

        refreshClick = RxView.clicks(mRefreshButton).share();

        mCompositeSubscription.add(suggestion1Stream());
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    private Observable<List<GithubUser>> responseStream() {
        final Retrofit retrofit = RetrofitManager.getInstance();
        return refreshClick
                .startWith((Void) null)
                .flatMap(new Func1<Void, Observable<List<GithubUser>>>() {
                    @Override public Observable<List<GithubUser>> call(Void aVoid) {
                        return retrofit.create(GithubService.class).users()
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    private Observable<GithubUser> makeSuggestion1Stream() {
        return Observable.combineLatest(
                RxView.clicks(mUserView1.findViewById(R.id.btn_close))
                        .startWith((Void) null),
                responseStream(),
                new Func2<Void, List<GithubUser>, GithubUser>() {
                    @Override public GithubUser call(Void aVoid, List<GithubUser> githubUsers) {
                        Timber.d("call %s", Thread.currentThread());
                        Random random = new Random(System.currentTimeMillis());
                        return githubUsers.get((int) Math.floor(random.nextInt(githubUsers.size())));
                    }
                })
                .mergeWith(
                        refreshClick.map(new Func1<Void, GithubUser>() {
                            @Override public GithubUser call(Void aVoid) {
                                return null;
                            }
                        })
                ).startWith((GithubUser) null);
    }

    private Subscription suggestion1Stream() {
        return makeSuggestion1Stream()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GithubUser>() {
                    @Override public void onCompleted() {
                        Timber.d("completed");
                    }

                    @Override public void onError(Throwable e) {
                        Timber.d("onError %s", e);
                    }

                    @Override public void onNext(GithubUser githubUser) {
                        if (githubUser == null) {
                            mUserView1.setVisibility(View.GONE);
                        } else {
                            mUserView1.setVisibility(View.VISIBLE);
                            ((TextView) mUserView1.findViewById(R.id.user_id)).setText("" + githubUser.id);
                            ((TextView) mUserView1.findViewById(R.id.user_name)).setText("" + githubUser.login);
                        }
                    }
                });
    }
}
