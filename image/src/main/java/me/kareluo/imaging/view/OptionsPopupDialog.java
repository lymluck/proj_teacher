package me.kareluo.imaging.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import me.kareluo.imaging.R;

/**
 * @author yqy
 * @date on 2018/3/1
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OptionsPopupDialog extends AlertDialog {
    private Context mContext;
    private ListView mListView;
    private List<String> arrays;
    private OptionsPopupDialog.OnOptionsItemClickedListener mItemClickedListener;

    public static OptionsPopupDialog newInstance(Context context, List<String> arrays) {
        OptionsPopupDialog optionsPopupDialog = new OptionsPopupDialog(context, arrays);
        return optionsPopupDialog;
    }

    public OptionsPopupDialog(Context context, List<String> arrays) {
        super(context);
        this.mContext = context;
        this.arrays = arrays;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_dialog_popup_options, (ViewGroup) null);
        this.mListView = view.findViewById(R.id.list_dialog_popup_options);
        ArrayAdapter<String> adapter = new ArrayAdapter(this.mContext, R.layout.image_dialog_popup_options_item, R.id.dialog_popup_item_name, this.arrays);
        this.mListView.setAdapter(adapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (OptionsPopupDialog.this.mItemClickedListener != null) {
                    OptionsPopupDialog.this.mItemClickedListener.onOptionsItemClicked(position);
                    OptionsPopupDialog.this.dismiss();
                }

            }
        });
        this.setContentView(view);
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = this.getPopupWidth();
        layoutParams.height = -2;
        this.getWindow().setAttributes(layoutParams);
    }

    public OptionsPopupDialog setOptionsPopupDialogListener(OptionsPopupDialog.OnOptionsItemClickedListener itemListener) {
        this.mItemClickedListener = itemListener;
        return this;
    }

    private int getPopupWidth() {
        int distanceToBorder = (int) this.mContext.getResources().getDimension(R.dimen.image_popup_dialog_distance_to_edge);
        return this.getScreenWidth() - 2 * distanceToBorder;
    }

    private int getScreenWidth() {
        return ((WindowManager) ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth();
    }

    public interface OnOptionsItemClickedListener {
        void onOptionsItemClicked(int var1);
    }

    public void addItem(String itme) {
        this.arrays.add(itme);
    }

    public void refreshItem() {
        ((ArrayAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
    }
}
