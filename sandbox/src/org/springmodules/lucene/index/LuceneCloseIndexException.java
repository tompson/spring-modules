/*
 * Cr�� le 11 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index;

import java.io.IOException;

import org.springframework.core.NestedRuntimeException;

/**
 * @author ttemplier
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LuceneCloseIndexException extends NestedRuntimeException {

	public LuceneCloseIndexException(String msg,IOException ex) {
		super(msg,ex);
	}

}
