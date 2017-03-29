package com.eduportal.entity;

//import com.eduportal.dao.UserDAO;

import com.eduportal.dao.UserDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ClientEntity extends UserEntity {
	private static final long serialVersionUID = -8053723799106370206L;
	private String orderid;
	private String curatorid;

	public final static String[] userParams = { "Серия и номер паспорта", "Имя латиницей (как в загран паспорте)",
			"Фамилия латиницей", "Серия и номер заграничного паспорта", "Срок действия заграничного паспорта",
			"Почтовый индекс", "Гражданство", "Номер школы", "Адрес школы", "Статус обучения (абитуриент, бакалавр)",
			"Год окончания обучения", "Место работы", "Оконченный ВУЗ (если имеется)", "Адрес ВУЗа (если имеется)",
			"ФИО отца", "ФИО матери", "Фамилия при рождении", "Ранее используемые фамилии", "Гражданство отца",
			"Гражданство матери", "Адрес прописки", "Адрес Skype", "Братья и сестры",
			"Предпологаемые университеты (минимум 2)",
			"Были ли ранее проблемы, связанные с пребыванием за границей ( "
					+ "нарушения визового режима или закона, депортации, задержания полицией, "
					+ "незаконный перевоз чего — либо и т п) (указать какие, если были",
			"Медицинские проблемы (болезни, которые требуют регулярный контроль врача, "
					+ "постоянное употребление каких-либо медикаментов) – для оптимального подбора страховой компании",
			"Противопоказания, аллергии, ограничение по спорту или активному образу жизни",
			"Спортивные секции, клубы, увлечения, что хотели бы продолжить в стране обучения",
			"Откуда узнали о нашей программе, кто нас порекомендовал", "Комментарий сотруднику Vedi Tour Group" };

	public ClientEntity(String string, String string2, String string3, String string4, String string5, String string6,
			Date date) {
		super(string, string2, string3, string4, string5, string6, date);
		this.orderid = null;
		this.entityClassName = "client";
	}

	public ClientEntity(UserEntity user) {
		HashMap<String, Method> other = new HashMap<>();
		HashMap<String, Method> had = new HashMap<>();
		for (Method meth : user.getClass().getMethods()) {
			if (meth.getName().startsWith("get")) {
				other.put(meth.getName().substring(3), meth);
				System.out.println("other: " + meth.getName());
			}
		}
		for (Method meth : this.getClass().getMethods()) {
			if (meth.getName().startsWith("set")) {
				had.put(meth.getName().substring(3), meth);
				System.out.println("this : " + meth.getName());
			}
		}
		for (String name : other.keySet()) {
			if (had.get(name) != null) {
				try {
					had.get(name).invoke(this, other.get(name).invoke(user));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					//do nothing :)
				}
			}
		}
		this.entityClassName = "client";
	}

	@Override
	public String getClassType() {
		return "client";
	}
	
	public boolean isFinal() {
		if (orderid == null) {
			return false;
		}
		return true;
	}

	public void defineCreator(Employee emp) {
		creator = emp.id;
	}

	public Employee getCurator() {
		return ((curatorid != null) ? UserDAO.getEmp(curatorid) : null);
	}
	
	public void defineCurator(UserEntity emp) {
		if (emp instanceof Employee == false) {
			return;
		}
		curatorid = emp.getId();
	}
	
	public String[][] getSimpleDataWithNull() {
		String[][] ret = new String[userParams.length][2];
		int i = 0;
		for (String param : userParams) {
			ret[i][0] = param;
			ret[i][1] = this.toMap().get(param);
			i++;
		}
		return ret;
	}


}
