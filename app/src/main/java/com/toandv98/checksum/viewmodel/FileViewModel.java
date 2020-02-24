package com.toandv98.checksum.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.toandv98.checksum.R;
import com.toandv98.checksum.data.AppExecutor;
import com.toandv98.checksum.data.entities.HashResult;
import com.toandv98.checksum.data.hash.HashFactory;
import com.toandv98.checksum.data.hash.HashType;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

import static com.toandv98.checksum.utils.Constants.EMPTY_TEXT;
import static com.toandv98.checksum.utils.Constants.SEPARATOR_DIR;

public class FileViewModel extends AndroidViewModel {

    private MutableLiveData<HashResult> mResult;
    private MutableLiveData<Boolean> mEqual;
    private MutableLiveData<String> mFilePath, mToast;
    private MutableLiveData<Boolean> mProgress;

    public FileViewModel(@NonNull Application application) {
        super(application);
        mResult = new MutableLiveData<>();
        mEqual = new MutableLiveData<>();
        mFilePath = new MutableLiveData<>();
        mProgress = new MutableLiveData<>();
        mToast = new MutableLiveData<>();
    }

    public LiveData<HashResult> getResult() {
        return mResult;
    }

    public LiveData<Boolean> getEqual() {
        return mEqual;
    }

    public LiveData<Boolean> getProgress() {
        return mProgress;
    }

    public LiveData<String> getFilePath() {
        return mFilePath;
    }

    public LiveData<String> getToast() {
        return mToast;
    }

    public void setFilePath(String[] path) {
        mFilePath.setValue(path.length > 0 ? path[0] : EMPTY_TEXT);
        mResult.setValue(null);
    }

    public void compare(CharSequence c) {
        mEqual.setValue(StringUtils.isNotBlank(c) && mResult.getValue() != null ? c.toString().equals(mResult.getValue().getResult()) : null);
    }

    public void check(int checkId) {
        mProgress.setValue(true);
        mResult.setValue(null);

        HashType type = null;
        switch (checkId) {
            case R.id.chip_md5:
                type = HashType.MD5;
                break;
            case R.id.chip_sha1:
                type = HashType.SHA1;
                break;
            case R.id.chip_sha256:
                type = HashType.SHA256;
                break;
            case R.id.chip_sha384:
                type = HashType.SHA384;
                break;
            case R.id.chip_sha512:
                type = HashType.SHA512;
                break;
        }
        if (type == null) {
            mToast.setValue(getApplication().getString(R.string.msg_select_hash_type));
            mProgress.setValue(false);
        } else if (StringUtils.isBlank(mFilePath.getValue())) {
            mToast.setValue(getApplication().getString(R.string.msg_select_file));
            mProgress.setValue(false);
        } else {
            hashFile(type);
        }
    }

    private void hashFile(HashType type) {

        AppExecutor.getInstance().execute(() -> {

            Uri uri = Uri.parse(SEPARATOR_DIR + mFilePath.getValue());
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                String hash = HashFactory.getHash(type).hash(inputStream);
                mResult.postValue(new HashResult(type, hash));
                mProgress.postValue(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
