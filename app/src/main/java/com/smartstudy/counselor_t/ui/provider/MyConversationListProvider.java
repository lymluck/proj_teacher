package com.smartstudy.counselor_t.ui.provider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.MyUserInfo;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by yqy on 2017/12/29.
 */
@ConversationProviderTag(
        conversationType = "private",
        portraitPosition = 1
)
public class MyConversationListProvider implements IContainerItemProvider.ConversationProvider<UIConversation> {
    private static final String TAG = "MyConversationListProvider";

    public MyConversationListProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View result = LayoutInflater.from(context).inflate(R.layout.fragment_item_base_conversation, (ViewGroup) null);
        MyConversationListProvider.ViewHolder holder = new MyConversationListProvider.ViewHolder();
        holder.title = (TextView) result.findViewById(R.id.rc_conversation_title);
        holder.time = (TextView) result.findViewById(R.id.rc_conversation_time);
        holder.content = (TextView) result.findViewById(R.id.rc_conversation_content);
        holder.notificationBlockImage = (ImageView) result.findViewById(R.id.rc_conversation_msg_block);
        holder.readStatus = (ImageView) result.findViewById(R.id.rc_conversation_status);
        holder.tvTag1 = result.findViewById(R.id.tv_tag);
        holder.tvTag2 = result.findViewById(R.id.tv_tag2);
        holder.tvTag3 = result.findViewById(R.id.tv_tag3);
        result.setTag(holder);
        return result;
    }

    @Override
    public void bindView(View view, int position, UIConversation data) {
        MyConversationListProvider.ViewHolder holder = (MyConversationListProvider.ViewHolder) view.getTag();
        ProviderTag tag = null;
        if (data == null) {
            holder.title.setText((CharSequence) null);
            holder.time.setText((CharSequence) null);
            holder.content.setText((CharSequence) null);
            holder.tvTag1.setVisibility(View.GONE);
            holder.tvTag2.setVisibility(View.GONE);
            holder.tvTag3.setVisibility(View.GONE);
        } else {
            holder.title.setText(data.getUIConversationTitle());
            UserInfo userInfo =  RongUserInfoManager.getInstance().getUserInfo(data.getConversationTargetId());
            MyUserInfo myUserInfo=null;
            if(userInfo instanceof MyUserInfo){
                myUserInfo=(MyUserInfo)myUserInfo;
            }
            if (myUserInfo != null) {
                if (TextUtils.isEmpty(myUserInfo.getAdmissionTime())) {
                    holder.tvTag1.setVisibility(View.GONE);
                } else {
                    holder.tvTag1.setText(myUserInfo.getAdmissionTime());
                }

                if (TextUtils.isEmpty(myUserInfo.getTargetCountry())) {
                    holder.tvTag2.setVisibility(View.GONE);
                } else {
                    holder.tvTag2.setText(myUserInfo.getTargetCountry());
                }


                if (TextUtils.isEmpty(myUserInfo.getTargetDegree())) {
                    holder.tvTag3.setVisibility(View.GONE);
                } else {
                    holder.tvTag3.setText(myUserInfo.getTargetDegree());
                }
            }
            String time = RongDateUtils.getConversationListFormatDate(data.getUIConversationTime(), view.getContext());
            holder.time.setText(time);
            if (TextUtils.isEmpty(data.getDraft()) && !data.getMentionedFlag()) {
                boolean readRec = false;

                try {
                    readRec = view.getResources().getBoolean(io.rong.imkit.R.bool.rc_read_receipt);
                } catch (Resources.NotFoundException var11) {
                    RLog.e("PrivateConversationProvider", "rc_read_receipt not configure in rc_config.xml");
                    var11.printStackTrace();
                }

                if (readRec) {
                    if (data.getSentStatus() == Message.SentStatus.READ && data.getConversationSenderId().equals(RongIM.getInstance().getCurrentUserId())) {
                        holder.readStatus.setVisibility(View.VISIBLE);
                    } else {
                        holder.readStatus.setVisibility(View.GONE);
                    }
                }

                Object cutStr;
                if (holder.content.getWidth() > 40 && data.getConversationContent() != null) {
                    cutStr = TextUtils.ellipsize(data.getConversationContent(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                } else {
                    cutStr = data.getConversationContent();
                }

                holder.content.setText((CharSequence) cutStr, TextView.BufferType.SPANNABLE);
            } else {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                SpannableString string;
                String preStr;
                CharSequence cutStr;
                if (data.getMentionedFlag()) {
                    preStr = view.getContext().getString(R.string.rc_message_content_mentioned);
                    if (holder.content.getWidth() > 0) {
                        cutStr = TextUtils.ellipsize(preStr + " " + data.getConversationContent(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                        string = new SpannableString(cutStr);
                    } else {
                        string = new SpannableString(preStr + " " + data.getConversationContent());
                    }

                    string.setSpan(new ForegroundColorSpan(view.getContext().getResources().getColor(io.rong.imkit.R.color.rc_mentioned_color)), 0, preStr.length(), 33);
                    builder.append(string);
                } else {
                    preStr = view.getContext().getString(R.string.rc_message_content_draft);
                    if (holder.content.getWidth() > 0) {
                        cutStr = TextUtils.ellipsize(preStr + " " + data.getDraft(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                        string = new SpannableString(cutStr);
                    } else {
                        string = new SpannableString(preStr + " " + data.getDraft());
                    }

                    string.setSpan(new ForegroundColorSpan(view.getContext().getResources().getColor(io.rong.imkit.R.color.rc_draft_color)), 0, preStr.length(), 33);
                    builder.append(string);
                }

                AndroidEmoji.ensure(builder);
                holder.content.setText(builder, TextView.BufferType.SPANNABLE);
                holder.readStatus.setVisibility(View.GONE);
            }

            if (RongContext.getInstance() != null && data.getMessageContent() != null) {
                tag = RongContext.getInstance().getMessageProviderTag(data.getMessageContent().getClass());
            }

            if (data.getSentStatus() != null && (data.getSentStatus() == Message.SentStatus.FAILED || data.getSentStatus() == Message.SentStatus.SENDING) && tag != null && tag.showWarning() && data.getConversationSenderId() != null && data.getConversationSenderId().equals(RongIM.getInstance().getCurrentUserId())) {
                Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), io.rong.imkit.R.drawable.rc_conversation_list_msg_send_failure);
                int width = bitmap.getWidth();
                Drawable drawable = null;
                if (data.getSentStatus() == Message.SentStatus.FAILED && TextUtils.isEmpty(data.getDraft())) {
                    drawable = view.getContext().getResources().getDrawable(R.drawable.rc_conversation_list_msg_send_failure);
                } else if (data.getSentStatus() == Message.SentStatus.SENDING && TextUtils.isEmpty(data.getDraft())) {
                    drawable = view.getContext().getResources().getDrawable(R.drawable.rc_conversation_list_msg_sending);
                }

                if (drawable != null) {
                    drawable.setBounds(0, 0, width, width);
                    holder.content.setCompoundDrawablePadding(10);
                    holder.content.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                }
            } else {
                holder.content.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            ConversationKey key = ConversationKey.obtain(data.getConversationTargetId(), data.getConversationType());
            Conversation.ConversationNotificationStatus status = RongContext.getInstance().getConversationNotifyStatusFromCache(key);
            if (status != null && status.equals(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB)) {
                holder.notificationBlockImage.setVisibility(View.VISIBLE);
            } else {
                holder.notificationBlockImage.setVisibility(View.GONE);
            }
        }

    }

    public Spannable getSummary(UIConversation data) {
        return null;
    }

    @Override
    public String getTitle(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);

        return userInfo == null ? userId : userInfo.getName();
    }

    @Override
    public Uri getPortraitUri(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        return userInfo == null ? null : userInfo.getPortraitUri();
    }


    protected class ViewHolder {
        public TextView title;
        public TextView time;
        public TextView content;
        public ImageView notificationBlockImage;
        public ImageView readStatus;
        public TextView tvTag1;
        public TextView tvTag2;
        public TextView tvTag3;

        protected ViewHolder() {
        }
    }
}
