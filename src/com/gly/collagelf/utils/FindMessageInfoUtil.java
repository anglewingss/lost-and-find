package com.gly.collagelf.utils;

import java.util.List;

import com.gly.collagelf.bean.MessageInfo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * �ӷ�������ȡ�����Ϣ
 * @author ������
 *
 */
public class FindMessageInfoUtil {

	/**
	 * ��ѯ�����Ϣ
	 * 
	 * @param type ����,1��Ĭ�ϲ�ѯ10�����ݣ������ӷ�������ȡ����/����ˢ��
	 *                 2��ʹ�ò���3����ѯcount������,����skip������/���ظ���
	 *                 3����ϲ���2����ѯһ������(����2���û�id)
	 *                 4����ϲ���2����ѯ��������(����2���û�ѡ���type)
	 *                 
	 * @param data
	 * @param skip  ��������������
	 * @param count  ��Ҫ��ѯ������������
	 * @param listener
	 */
	public static void findMessageInfos(
			int type,
			Object data,
			int skip,
			int count,
			final FindMessageInfoListener listener){
		BmobQuery<MessageInfo> query = new BmobQuery<MessageInfo>();
		switch (type) {
		case 1:
			//������ѯ����ҳ������ˢ��
//			query.setLimit(10);
			query.order("-createdAt");
			break;

		case 2://��ҳ�ļ��ظ���
			query.setSkip(skip);
			break;
			
		case 3://��ѯָ��id�Ļ
			query.addWhereEqualTo("objectId", data);
			
			break;
		case 4://����Ʒ���Ͳ�ѯ
			query.addWhereEqualTo("messageGoodsType", data);
			query.order("-createdAt");
			break;
		case 5://ʧ����������ѯ
			query.addWhereEqualTo("messageType", data);
			query.order("-createdAt");
			break;
		case 6://ʧ����������е�����ˢ��
			query.addWhereEqualTo("messageType", data);
			query.order("-createdAt");
			break;
		case 7://ʧ����������еļ��ظ���
			query.addWhereEqualTo("messageType", data);
			query.setSkip(skip);
			query.order("-createdAt");
			break;
		}
		query.findObjects(new FindListener<MessageInfo>() {
			
			@Override
			public void done(List<MessageInfo> info, BmobException ex) {
				listener.getMessageInfo(info,ex);
			}
		});
	}
	
	//�۲���ģʽ��˭��ѯ˭ʵ�ֽӿ�
	public interface FindMessageInfoListener{
		void getMessageInfo(List<MessageInfo> messageInfos,BmobException ex);
	}
}
