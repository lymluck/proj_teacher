package com.smartstudy.counselor_t.ui.fragment;

import android.content.Context;

import com.smartstudy.counselor_t.ui.adapter.MyConversationAdapter;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.adapter.MessageListAdapter;

/**
 * @author louis
 * @date on 2018/2/1
 * @describe 自定义聊天列表形式
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class MyConversationFragment extends ConversationFragment {

    @Override
    public MessageListAdapter onResolveAdapter(Context context) {
        return new MyConversationAdapter(context);
    }


}
