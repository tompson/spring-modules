/*
 * Cr�� le 21 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index.core;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.factory.IndexFactory;

/**
 * @author ttemplier
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ResourceAwareIndexFactoryProxy implements IndexFactory {

	private IndexFactory target;

	/* (non-Javadoc)
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		// TODO Raccord de m�thode auto-g�n�r�
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 */
	public IndexWriter getIndexWriter() {
		// TODO Raccord de m�thode auto-g�n�r�
		return null;
	}

	/**
	 * @return
	 */
	public IndexFactory getTargetIndexFactory() {
		return target;
	}

	/**
	 * @param factory
	 */
	public void setTargetIndexFactory(IndexFactory factory) {
		target = factory;
	}

}
