/*
 * Cr�� le 30 juin 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.springmodules.samples.lucene.bean.indexing;

/**
 * @author ttemplier
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class DocumentField {
	private String name;
	private String value;
	private boolean indexed;
	private boolean stored;

	public DocumentField(String name,String value,boolean indexed,boolean stored) {
		this.name=name;
		this.value=value;
		this.indexed=indexed;
		this.stored=stored;
	}

	/**
	 * @return
	 */
	public boolean isIndexed() {
		return indexed;
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
	public boolean isStored() {
		return stored;
	}

	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param b
	 */
	public void setIndexed(boolean b) {
		indexed = b;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param b
	 */
	public void setStored(boolean b) {
		stored = b;
	}

	/**
	 * @param string
	 */
	public void setValue(String string) {
		value = string;
	}

}
