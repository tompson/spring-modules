/*
 * Cr�� le 12 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.search.Searcher;

/**
 * @author ttemplier
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface SearcherFactory {
	public Searcher getSearcher() throws IOException;
}
