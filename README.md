<h2>ES-LETI-1Sem-2021-Grupo9</h2>

<b>Projeto realizado por:</b>
<ul>
<li>Tatiana Velosa, <b>92556</b></li>
<li>Henrique de Sousa, <b>93540</b></li>
<li>Gonçalo Rodrigues, <b>92600</b></li>
<li>Paulo Palma, <b>69234</b></li>
</ul>

Todas as funcionalidades foram implementadas, embora a performance possa ser melhorada.

<b>Erros ou warnings (codesmells):</b>
<ul>
<li><i>Long Method</i> no método <code>getNotCommittedActivitiesByMember</code> do TrelloManager:</li>
<code>for (Card card : memberMeetingList) { totalHours += getCardHours(card.getId())[0]; }</code>
<p>Foi escolhido deixar esta linha de código no método apesar do Long Method warning, visto que se trata da soma das 
horas totais e não teria qualquer utilidade para outros métodos na classe.</p>
<li>Não foram efetuados testes para as classes <code>HelloApplication</code>, <code>HelloController</code> e 
<code>TableData</code> por serem classes relacionadas com a interface gráfica.</li>
</ul>
