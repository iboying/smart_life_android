package com.tallty.smart_life_android.fragment.Pop;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;

/**
 * Created by kang on 16/7/30.
 * 全局提示弹窗
 */
public class HintDialogFragment extends DialogFragment implements View.OnClickListener {
    private ImageView cancel_btn;
    private TextView confirm_btn;
    private ImageView hint_image;
    private TextView hint_text;

    private String hint;

    private OnHintDialogEventListener onHintDialogEventListener;

    public static HintDialogFragment newInstance(String hint) {
        Bundle args = new Bundle();
        args.putString("提示", hint);
        HintDialogFragment fragment = new HintDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnHintDialogEventListener {
        void onOk(TextView confirm_btn);
        void onCancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            hint = args.getString("提示");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_hint_dialog);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hint_dialog, container, false);
        
        cancel_btn = (ImageView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        hint_image = (ImageView) view.findViewById(R.id.hint_icon);
        hint_text = (TextView) view.findViewById(R.id.hint_text);

        Glide.with(getActivity()).load(R.drawable.snack_bar_icon).into(hint_image);
        hint_text.setText(hint);
        
        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 窗体大小,动画,弹出方向
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.dialogStyle;
            getDialog().getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                onHintDialogEventListener.onCancel();
                break;
            case R.id.confirm_btn:
                onHintDialogEventListener.onOk(confirm_btn);
                break;
        }
    }

    public void setOnHintDialogEventListener(OnHintDialogEventListener listener) {
        this.onHintDialogEventListener = listener;
    }
}
