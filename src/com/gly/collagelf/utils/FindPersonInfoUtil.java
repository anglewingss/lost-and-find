package com.gly.collagelf.utils;

import java.util.List;

import com.gly.collagelf.bean.Person;

import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * ʹ�ù۲���ģʽ
 * �ӷ�������ȡ�û���Ϣ�Ĺ�����
 * @author ������
 *
 */
public class FindPersonInfoUtil {

	public static void findPersonInfo(String personObjectId,final FindPersonInfoListener listener){
		BmobQuery<Person> query = new BmobQuery<Person>();
//		Log.i("bmob","��������personObjectId��"+personObjectId.toString());
/*		query.getObject(personObjectId, new QueryListener<Person>() {

		    @Override
		    public void done(Person person, BmobException e) {
		        if(e==null){
		        	listener.getPersonInfo(person);
		        	
		        	 Log.i("bmob","find user success��"+person.toString());
		        	 for (int i = 0; i < person.getPraiseAction().size(); i++) {
		        		 Log.i("bmob", person.getPraiseAction().get(i).toString());	
					}
		        }else{
		            Log.i("bmob","��ѯ�û�ʧ�ܡ�������"+e.getMessage()+","+e.getErrorCode());
		        }
		    }

		});*/
		query.addWhereEqualTo("objectId", personObjectId);
		query.findObjects(new FindListener<Person>() {
		    @Override
		    public void done(List<Person> person,BmobException e) {
		        if(e==null){
		        	listener.getPersonInfo(person);
//		            toast("��ѯ�û��ɹ�:"+object.size());
		            Log.i("bmob","find user success��"+person.size()+person.toString());
		        }else{
		            //toast("�����û���Ϣʧ��:" + e.getMessage());
		        	Log.i("bmob","�����û���Ϣʧ�ܣ�"+e.getMessage());
		        }
		    }
		});
	}
	
	//�۲���ģʽ
	public interface FindPersonInfoListener{
		void getPersonInfo(List<Person> person);
	}
}
