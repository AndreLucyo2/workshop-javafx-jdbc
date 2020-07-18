package gui.listeners;

public interface DataChangeListener {

	/*
	 * Seção 23 - 282. Padrão de projeto Observer para atualizar a TableView DataChangeListener
	 * interface que permite um objeto escutar eventos de outro objeto, ( observer )objeto que altera os
	 * dados emite um evento, alertando quem esta na escuta
	 */

	// Evento para ser disparado quando os dados mudarem
	void onDataChanged();
}
