package com.app.picviewr.presenter

import Photo
import com.app.picviewr.api.ApiService
import com.app.picviewr.fragment.HomeFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomePresenter (private var view: HomeFragment, private var apiService: ApiService) {
    private var composite : CompositeDisposable = CompositeDisposable()

    fun getPhotoList(page: Int){
        apiService.getPhotoList(page, 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    composite.add(d)
                }

                override fun onNext(response: List<Photo>) {
                    view.updateView(response as ArrayList<Photo>, page)
                }

                override fun onError(t: Throwable) {
                    view.handleError(t)
                }
            })
    }

    fun unsubscribe() {
        composite.dispose()
    }
}