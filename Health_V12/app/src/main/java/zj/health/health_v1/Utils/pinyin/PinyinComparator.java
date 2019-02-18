package zj.health.health_v1.Utils.pinyin;


import java.util.Comparator;

import zj.health.health_v1.Model.Contacts;

public class PinyinComparator implements Comparator<Contacts> {

	@Override
	public int compare(Contacts lhs, Contacts rhs) {
		// TODO Auto-generated method stub
		return sort(lhs, rhs);
	}

	private int sort(Contacts lhs, Contacts rhs) {
		// 获取ascii值
		String a = lhs.getFirstPinYin();
		String b = rhs.getFirstPinYin();
		int lhs_ascii = lhs.getFirstPinYin().toUpperCase().charAt(0);
		int rhs_ascii = rhs.getFirstPinYin().toUpperCase().charAt(0);
		// 判断若不是字母，则排在字母之后
		if (lhs_ascii < 65 || lhs_ascii > 90)
			return 1;
		else if (rhs_ascii < 65 || rhs_ascii > 90)
			return -1;
		else
			return lhs.getFirstPinYin().compareTo(rhs.getFirstPinYin());
	}

}
