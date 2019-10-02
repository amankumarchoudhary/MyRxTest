package com.example.myrxtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RecyclerView rview;
    Button btn1;
    EditText et1;
    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=findViewById(R.id.btn1);
        et1=findViewById(R.id.et1);
        rview=findViewById(R.id.rview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rview.setLayoutManager(linearLayoutManager);

        tasks = new ArrayList<>();
        tasks.add(new Task("Take out the trash", true, 3));
        tasks.add(new Task("Walk the dog", false, 2));
        tasks.add(new Task("Make my bed", true, 1));
        tasks.add(new Task("Unload the dishwasher", false, 4));
        tasks.add(new Task("Make dinner", true, 5));

        final Observable<Task> taskObservable = Observable // create a new Observable object
                .fromIterable(tasks)
                /*.create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {
                        // Inside the subscribe method iterate through the list of tasks and call onNext(task)
                        for(Task task: DataSource.createTasksList()){
                            if(!emitter.isDisposed()){
                                emitter.onNext(task);
                            }
                        }
                        // Once the loop is complete, call the onComplete() method
                        if(!emitter.isDisposed()){
                            emitter.onComplete();
                        }

                    }
                })*/
                /*.just(new Task("one",true,1),
                        new Task("two",true,1),
                        new Task("three",true,1),
                        new Task("four",true,1),
                        new Task("five",true,1),
                        new Task("six",true,1),
                        new Task("seven",true,1),
                        new Task("eight",true,1),
                        new Task("nine",true,1),
                        new Task("ten",true,1))*/
                /*.map(new Function<Task, Task>() {
                    @Override
                    public Task apply(Task task) throws Exception {
                        Task task1=new Task(task.getDescription()+"Aman",task.isComplete(),task.getPriority());
                        return task1;
                    }
                })*/
//                .buffer(2)
//                .debounce(1,TimeUnit.NANOSECONDS)
//                .throttleLatest(1,TimeUnit.NANOSECONDS)
                .subscribeOn(Schedulers.io());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Task task) { // run on main thread

                Log.d("ondone", "onNext0: " + task.getDescription());

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });

//        taskObservable.subscribe(new Observer<List<Task>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(List<Task> tasks) {
//                Log.d("ondone", "onNext: : " + tasks.size());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        Observable.range(0,6)
                .repeat(3)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("ondone", "onNext1: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        // emit an observable every time interval
        Observable<Long> intervalObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile(new Predicate<Long>() { // stop the process if more than 5 seconds passes
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong <= 5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        intervalObservable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Long aLong) {
                Log.d("ondone", "onNext2: interval: " + aLong);
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });

        // emit single observable after a given delay
        Observable<Long> timeObservable = Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io());

        timeObservable.subscribe(new Observer<Long>() {

            long time = 0; // variable for demonstating how much time has passed

            @Override
            public void onSubscribe(Disposable d) {
                time = System.currentTimeMillis() / 1000;
            }
            @Override
            public void onNext(Long aLong) {
                Log.d("ondone", "onNext: " + ((System.currentTimeMillis() / 1000) - time) + " seconds have elapsed." );
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tasks.add(new Task(et1.getText().toString(),false,5));
            }
        });

        Flowable.range(0, 1000000)
                .onBackpressureBuffer()
                .observeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        Log.d("ondone", "onNext: " + integer);
                    }
                    @Override
                    public void onError(Throwable t) {
                        Log.e("ondone", "onError: ", t);
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
}
