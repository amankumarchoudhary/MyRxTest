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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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

                Log.d("ondone", "onNext: : " + task.getDescription());

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
