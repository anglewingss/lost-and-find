package com.gly.collagelf.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/**
 * �û���������Ϣ
 * @author ������
 *
 */
public class MessageInfo extends BmobObject{

	private String messageType;//�����  �û�ѡ�� ````
	private String messageTitle;//�����   �û����� ````
	private String messageUserId;//������û�Id ������ѯ�û���  ϵͳ�Զ���ȡ����
	private String messageUsername;//��ϵ��
	private String messagePhone;//��ϵ�绰
	private String messageGoodsType;//��Ʒ������
	
	private String messageAddress;//��ص�   �û�ѡ��/���� 
	private String messageDesc;//���ϸ����  �û����� ````
	private BmobGeoPoint messageLocation;//���γ��   �û�ѡ��/����
	private List<String> praiseUsers;//��¼���޵��û���  //ϵͳ¼��
//	private List<UserMsg> msgs;//�û���������   //ϵͳ¼��
	private List<BmobFile> messagePics;//�ͼƬ    //�û��ϴ�````
	private Boolean flag;// ��ǰ�״̬(�Ƿ�ʼ)   ϵͳ¼��
	private Integer loveCount;//����ղصĴ�����Integer�������ͣ���Ҫ��int���ͣ�
	public MessageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageInfo(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageName() {
		return messageTitle;
	}
	public void setMessageName(String messageName) {
		this.messageTitle = messageName;
	}
	public String getMessageUserId() {
		return messageUserId;
	}
	public void setMessageUserId(String messageUserId) {
		this.messageUserId = messageUserId;
	}
	public String getMessageAddress() {
		return messageAddress;
	}
	public void setMessageAddress(String messageAddress) {
		this.messageAddress = messageAddress;
	}
	public String getMessageDesc() {
		return messageDesc;
	}
	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
	public BmobGeoPoint getMessageLocation() {
		return messageLocation;
	}
	public void setMessageLocation(BmobGeoPoint messageLocation) {
		this.messageLocation = messageLocation;
	}
	public List<String> getPraiseUsers() {
		return praiseUsers;
	}
	public void setPraiseUsers(List<String> praiseUsers) {
		this.praiseUsers = praiseUsers;
	}
	public List<BmobFile> getMessagePics() {
		return messagePics;
	}
	public void setMessagePics(List<BmobFile> messagePics) {
		this.messagePics = messagePics;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public Integer getLoveCount() {
		return loveCount;
	}
	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	@Override
	public String toString() {
		return "MessageInfo [messageType=" + messageType + ", messageTitle="
				+ messageTitle + ", messageUserId=" + messageUserId
				+ ", messageUsername=" + messageUsername + ", messagePhone="
				+ messagePhone + ", messageGoodsType=" + messageGoodsType
				+ ", messageAddress=" + messageAddress + ", messageDesc="
				+ messageDesc + ", messageLocation=" + messageLocation
				+ ", praiseUsers=" + praiseUsers + ", messagePics="
				+ messagePics + ", flag=" + flag + ", loveCount=" + loveCount
				+ "]";
	}
	public String getMessageUsername() {
		return messageUsername;
	}
	public void setMessageUsername(String messageUsername) {
		this.messageUsername = messageUsername;
	}
	public String getMessagePhone() {
		return messagePhone;
	}
	public void setMessagePhone(String messagePhone) {
		this.messagePhone = messagePhone;
	}
	public String getMessageGoodsType() {
		return messageGoodsType;
	}
	public void setMessageGoodsType(String messageGoodsType) {
		this.messageGoodsType = messageGoodsType;
	}
	
}
