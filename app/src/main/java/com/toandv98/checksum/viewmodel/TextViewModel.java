package com.toandv98.checksum.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.toandv98.checksum.data.AppExecutor;
import com.toandv98.checksum.data.entities.HashResult;
import com.toandv98.checksum.data.hash.HashFactory;
import com.toandv98.checksum.data.hash.HashType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class TextViewModel extends ViewModel {

    private MutableLiveData<List<HashResult>> mResults;
    private MutableLiveData<HashResult> mEqual;

    public TextViewModel() {
        mResults = new MutableLiveData<>();
        mEqual = new MutableLiveData<>();
    }

    public void setInput(CharSequence c) {
        if (StringUtils.isNotBlank(c)) {
            hashAll(c.toString());
        } else {
            mResults.setValue(new ArrayList<>());
        }
    }

    public LiveData<List<HashResult>> getResults() {
        return mResults;
    }

    private void hashAll(String input) {
        AppExecutor.getInstance().execute(() -> {
            List<HashResult> results = new ArrayList<>();
            for (HashType type : HashType.values()) {
                results.add(new HashResult(type, HashFactory.getHash(type).hash(input)));
            }
            mResults.postValue(results);
        });
    }

    public LiveData<HashResult> getEqual() {
        return mEqual;
    }

    public void setEqual(CharSequence compare) {
        if (StringUtils.isNotBlank(compare) && mResults.getValue() != null) {
            for (HashResult h : mResults.getValue()) {
                if (compare.toString().equals(h.getResult())) {
                    mEqual.setValue(h);
                    return;
                }
            }
            mEqual.setValue(new HashResult(null, null));
        } else {
            mEqual.setValue(null);
        }
    }
}
