/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.springmodules.samples.jrules.model;

/**
 * @author ttemplier
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class Car {
	public long id;
	public String name;
	public String mark;
	public String model;
	public int year;
	public double price;
	public boolean goodBargain;

	public Car() {
	}

	/**
	 * @return
	 */
	public boolean isGoodBargain() {
		return goodBargain;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param b
	 */
	public void setGoodBargain(boolean b) {
		goodBargain = b;
		System.out.println("!! car.goodBargain = "+goodBargain);
	}

	/**
	 * @param l
	 */
	public void setId(long l) {
		id = l;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param d
	 */
	public void setPrice(double d) {
		price = d;
	}

	/**
	 * @return
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * @return
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param string
	 */
	public void setMark(String string) {
		mark = string;
	}

	/**
	 * @param string
	 */
	public void setModel(String string) {
		model = string;
	}

	/**
	 * @param i
	 */
	public void setYear(int i) {
		year = i;
	}

}
