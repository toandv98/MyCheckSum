package com.toandv98.checksum.ui;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.material.chip.ChipGroup;
import com.toandv98.checksum.R;
import com.toandv98.checksum.viewmodel.FileViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.toandv98.checksum.utils.Constants.DEFAULT_DIR;
import static com.toandv98.checksum.utils.Constants.EMPTY_TEXT;

public class FileFragment extends Fragment {

    @BindView(R.id.edt_file_path)
    EditText mEdtPath;
    @BindView(R.id.edt_file_compare)
    EditText mEdtCompare;
    @BindView(R.id.btn_file_check)
    Button mBtnCheck;
    @BindView(R.id.tv_file_equal)
    TextView mTvEqual;
    @BindView(R.id.tv_file_label)
    TextView mTvLabel;
    @BindView(R.id.tv_file_result)
    TextView mTvResult;
    @BindView(R.id.pb_hash_file)
    ProgressBar mPbLoading;
    @BindView(R.id.chip_group_type)
    ChipGroup mChipGroup;

    private FileViewModel mViewModel;
    private FilePickerDialog mDialog;
    private ClipboardManager mClipboard;

    public FileFragment() {
    }

    public static FileFragment newInstance() {
        return new FileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_clear) {
            mViewModel.setFilePath(new String[0]);
            mEdtCompare.setText(EMPTY_TEXT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FileViewModel.class);
        mClipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        initChooser();
        subscribeObserver();
    }

    private void subscribeObserver() {

        mViewModel.getResult().observe(getViewLifecycleOwner(), hashResult -> {
            if (hashResult != null) {
                mBtnCheck.setText(R.string.text_button_check);
                mTvLabel.setVisibility(View.VISIBLE);
                mTvResult.setVisibility(View.VISIBLE);
                mTvLabel.setText(hashResult.getType().name());
                mTvResult.setText(hashResult.getResult());
                mViewModel.compare(mEdtCompare.getText());
            } else {
                mTvLabel.setVisibility(View.INVISIBLE);
                mTvResult.setVisibility(View.INVISIBLE);
            }
        });

        mViewModel.getEqual().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) if (aBoolean) {
                mEdtCompare.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorEqual));
                mTvEqual.setText(getString(R.string.text_equal));
                mTvEqual.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_all, 0, 0, 0);
                mTvEqual.setVisibility(View.VISIBLE);
            } else {
                mEdtCompare.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorNotEqual));
                mTvEqual.setText(R.string.text_not_equal);
                mTvEqual.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_error_outline, 0, 0, 0);
            }
            else {
                mTvEqual.setVisibility(View.INVISIBLE);
                mEdtCompare.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color));
            }
        });

        mViewModel.getFilePath().observe(getViewLifecycleOwner(), s -> mEdtPath.setText(s));

        mViewModel.getProgress().observe(getViewLifecycleOwner(), aBoolean -> {
            mBtnCheck.setVisibility(aBoolean ? View.INVISIBLE : View.VISIBLE);
            mPbLoading.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
        });

        mViewModel.getToast().observe(getViewLifecycleOwner(), s -> Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show());
    }

    private void initChooser() {
        DialogProperties properties = new DialogProperties();
        properties.root = new File(DEFAULT_DIR);
        mDialog = new FilePickerDialog(requireActivity(), properties);
        mDialog.setTitle(R.string.title_dialog_chooser);
        mDialog.setDialogSelectionListener(files -> mViewModel.setFilePath(files));
    }

    @OnClick(R.id.btn_file_choose)
    void onChooseClick() {
        mDialog.show();
    }

    @OnClick(R.id.btn_file_check)
    void onCheckClick() {
        mViewModel.check(mChipGroup.getCheckedChipId());
    }

    @OnTextChanged(R.id.edt_file_compare)
    void onTextChanged(CharSequence c) {
        mViewModel.compare(c);
    }

    @OnClick(R.id.btn_file_paste)
    void onClickPaste() {
        ClipData data = mClipboard.getPrimaryClip();
        if (data != null) {
            mEdtCompare.setText(data.getItemAt(0).getText());
            mEdtCompare.requestFocus();
            mEdtCompare.setSelection(mEdtCompare.getText().length());
        }
    }

    @OnClick(R.id.tv_file_result)
    void onCopyClick() {
        ClipData clipData = ClipData.newPlainText(mTvLabel.getText(), mTvResult.getText());
        mClipboard.setPrimaryClip(clipData);
        Toast.makeText(requireActivity(), R.string.msg_copied, Toast.LENGTH_SHORT).show();
    }
}
