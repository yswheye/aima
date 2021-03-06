package net.oschina.app.v2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 通知信息实体类
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Notice implements Serializable, Parcelable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "oschina";

    public final static int TYPE_ATME = 1;
    public final static int TYPE_MESSAGE = 2;
    public final static int TYPE_COMMENT = 3;
    public final static int TYPE_NEWFAN = 4;

    public static final String NODE_NOTICE = "notice";
    public static final String NODE_ATME_COUNT = "atmeCount";
    public static final String NODE_MESSAGE_COUNT = "msgCount";
    public static final String NODE_REVIEW_COUNT = "reviewCount";
    public static final String NODE_NEWFANS_COUNT = "newFansCount";

    private int atmeCount;
    private int msgCount;
    private int reviewCount;
    private int newFansCount;

    public int getAtmeCount() {
        return atmeCount;
    }

    public void setAtmeCount(int atmeCount) {
        this.atmeCount = atmeCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getNewFansCount() {
        return newFansCount;
    }

    public void setNewFansCount(int newFansCount) {
        this.newFansCount = newFansCount;
    }

    public static Notice parse(InputStream inputStream) throws IOException, AppException {
        Notice notice = null;
        //获得XmlPullParser解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        try {
            xmlParser.setInput(inputStream, UTF8);
            //获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType = xmlParser.getEventType();
            //一直循环，直到文档结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                String tag = xmlParser.getName();
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        //通知信息
                        if (tag.equalsIgnoreCase(NODE_NOTICE)) {
                            notice = new Notice();
                        } else if (notice != null) {
                            if (tag.equalsIgnoreCase(NODE_ATME_COUNT)) {
                                notice.setAtmeCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase(NODE_MESSAGE_COUNT)) {
                                notice.setMsgCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase(NODE_REVIEW_COUNT)) {
                                notice.setReviewCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase(NODE_NEWFANS_COUNT)) {
                                notice.setNewFansCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                //如果xml没有结束，则导航到下一个节点
                evtType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            throw AppException.xml(e);
        } finally {
            inputStream.close();
        }
        return notice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.atmeCount);
        dest.writeInt(this.msgCount);
        dest.writeInt(this.reviewCount);
        dest.writeInt(this.newFansCount);
    }

    public Notice() {
    }

    private Notice(Parcel in) {
        this.atmeCount = in.readInt();
        this.msgCount = in.readInt();
        this.reviewCount = in.readInt();
        this.newFansCount = in.readInt();
    }

    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>() {
        public Notice createFromParcel(Parcel source) {
            return new Notice(source);
        }

        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };
}
