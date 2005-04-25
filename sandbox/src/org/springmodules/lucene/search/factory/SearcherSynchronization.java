/*
 * Cr�� le 21 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.factory;

import org.springmodules.resource.support.ResourceSynchronizationAdapter;

/**
 * Callback for resource cleanup at the end of an use of index search.
 */
public class SearcherSynchronization extends ResourceSynchronizationAdapter {

	private final SearcherHolder searcherHolder;

	private final SearcherFactory searcherFactory;

	public SearcherSynchronization(SearcherHolder searcherHolder, SearcherFactory searcherFactory) {
		this.searcherHolder = searcherHolder;
		this.searcherFactory = searcherFactory;
	}

}
