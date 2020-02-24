package com.toandv98.checksum.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toandv98.checksum.R;
import com.toandv98.checksum.viewmodel.TextViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.toandv98.checksum.utils.Constants.EMPTY_TEXT;

public class TextFragment extends Fragment {

    @BindView(R.id.edt_text_input)
    EditText mEdtInput;
    @BindView(R.id.edt_text_compare)
    EditText mEdtCompare;
    @BindView(R.id.rv_hash_text_result)
    RecyclerView mRvResult;
    @BindView(R.id.tv_text_equal)
    TextView mTvEqual;

    private TextViewModel mViewModel;
    private ClipboardManager mClipboard;
    private ResultAdapter mAdapter;

    public TextFragment() {
    }

    public static TextFragment newInstance() {
        return new TextFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_clear) {
            mEdtInput.setText(EMPTY_TEXT);
            mEdtCompare.setText(EMPTY_TEXT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(TextViewModel.class);
        mClipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        mAdapter = new ResultAdapter(requireContext(), new ArrayList<>());
        mRvResult.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mRvResult.setAdapter(mAdapter);

        subscribeObserver();
    }

    private void subscribeObserver() {

        mViewModel.getResults().observe(this, hashResults -> {
            if (hashResults.isEmpty()) {
                mEdtInput.requestFocus();
                mTvEqual.setVisibility(View.GONE);
            }
            mRvResult.scheduleLayoutAnimation();
            mAdapter.updateData(hashResults);
            mViewModel.setEqual(mEdtCompare.getText());
        });

        mViewModel.getEqual().observe(this, hashEqual -> {
            mAdapter.setEqual(hashEqual);
            if (hashEqual != null) if (hashEqual.getResult() == null) {
                mEdtCompare.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorNotEqual));
                mTvEqual.setText(R.string.text_not_equal);
                mTvEqual.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_error_outline, 0, 0, 0);
            } else {
                mEdtCompare.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorEqual));
                mTvEqual.setText(String.format("%s %s", getString(R.string.text_equal), hashEqual.getType().name()));
                mTvEqual.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_all, 0, 0, 0);
                mTvEqual.setVisibility(View.VISIBLE);
            }else {
                mTvEqual.setVisibility(View.GONE);
            }
        });
    }

    @OnTextChanged(R.id.edt_text_input)
    void onTextChanged(CharSequence s) {
        mViewModel.setInput(s);
    }

    @OnTextChanged(R.id.edt_text_compare)
    void onCompareChanged(CharSequence s) {
        mViewModel.setEqual(s);
    }

    @OnClick(R.id.btn_text_paste_input)
    void onPasteClick() {
        paste(mEdtInput);
    }

    @OnClick(R.id.btn_text_paste_compare)
    void onPasteCompareClick() {
        paste(mEdtCompare);
    }

    private void paste(EditText e) {
        ClipData data = mClipboard.getPrimaryClip();
        if (data != null) {
            e.setText(data.getItemAt(0).getText());
            e.requestFocus();
            e.setSelection(e.getText().length());
        }
    }
}
