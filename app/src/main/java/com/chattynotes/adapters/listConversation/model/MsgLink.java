package com.chattynotes.adapters.listConversation.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.chattynotes.constant.MsgConstant;
//import com.chattynotes.xmpp.extensions.LinkExtension;
//import org.jivesoftware.smack.packet.ExtensionElement;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.util.PacketParserUtils;

public class MsgLink implements Parcelable {

    final String lTitle;
    final String lDes;
    final String lUrl;
    final String lThumb;

    public MsgLink(String lTitle, String lDes, String lUrl, String lThumb) {
        this.lTitle = lTitle;
        this.lDes = lDes;
        this.lUrl = lUrl;
        this.lThumb = lThumb;
    }

    public static MsgLink convertToObj(String XMLStanza) {
//        if(!XMLStanza.equals(MsgConstant.DEFAULT_LINK_MESSAGE)) {
//            try {
//                LinkExtension linkExt = (LinkExtension) PacketParserUtils.parseExtensionElement(LinkExtension.ELEMENT, LinkExtension.NAMESPACE, PacketParserUtils.getParserFor(XMLStanza));
//                return new MsgLink(linkExt.getLinkTitle(), linkExt.getLinkDescription(), linkExt.getLinkUrl(), linkExt.getLinkThumb());
//            } catch (Exception ignored) {
//            }
//        }
        return null;
    }

    //temporary used
    public static String convertToString(String msgString) {
//        try {
//            Message message = (Message) PacketParserUtils.parseStanza(msgString);
//            ExtensionElement packetExtension = message.getExtension(LinkExtension.NAMESPACE);
//            LinkExtension linkExt = (LinkExtension)packetExtension;
//            return linkExt.toXML().toString();
//        } catch (Exception ignored) {
//        }
        return MsgConstant.DEFAULT_LINK_MESSAGE;
    }

    public static String convertToString(MsgLink linkObj) {
//        if(linkObj!=null) {
//            LinkExtension linkExt = new LinkExtension();
//            linkExt.setLinkTitle(linkObj.lTitle);
//            linkExt.setLinkDescription(linkObj.lDes);
//            linkExt.setLinkUrl(linkObj.lUrl);
//            linkExt.setLinkThumb(linkObj.lThumb);
//            return linkExt.toXML().toString();
//        }
        return MsgConstant.DEFAULT_LINK_MESSAGE;
    }


    //__________________________________________________________________________________________________ Parcelable
    private MsgLink(Parcel in) {
        lTitle = in.readString();
        lDes = in.readString();
        lUrl = in.readString();
        lThumb = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lTitle);
        dest.writeString(lDes);
        dest.writeString(lUrl);
        dest.writeString(lThumb);
    }

    public static final Creator<MsgLink> CREATOR = new Creator<MsgLink>() {
        @Override
        public MsgLink createFromParcel(Parcel in) {
            return new MsgLink(in);
        }

        @Override
        public MsgLink[] newArray(int size) {
            return new MsgLink[size];
        }
    };
}