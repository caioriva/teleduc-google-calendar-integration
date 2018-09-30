/*
 * GrafoPolar.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

import java.util.*;
import java.awt.*;
import java.awt.geom.Point2D;
import grafo.*;

/** Classe que armazena um grafo como a estrutura de um Grafo Polar.
 * Um Grafo Polar apresenta dois an�is conc�ntricos em que os n�s de um grafo podem
 * ser dispostos.
 */
public class GrafoPolar extends Grafo implements Aneis {
    
    /** Cor do c�rculo perif�rico.
     */
    public Color corAreaPeriferica;
    
    /** Cor do c�rculo central.
     */
    public Color corAreaCentral;
    
    /** Cor da �rea proibida.
     */
    public Color corAreaProibida;
    
    /** Hash map de arestas.
     */
    private Map arestasHash = new HashMap();
    
    /** Raio do anel perif�rico.
     */
    public ObservableInt raioPeriferico;
    
    /** Raio do anel central.
     */
    public ObservableInt raioCentral;
    
    /** Raio da �rea proibida.
     */
    public ObservableInt raioAreaProibida;
    
    /** �ngulo usando para girar anel central. � o �ngulo que orienta os demais
     * n�s do anel.
     */
    public ObservableAngle anguloInicialNosCentrais;
    
    /** �ngulo usando para girar anel perif�rico. � o �ngulo que orienta os
     * demais n�s do anel.
     */
    public ObservableAngle anguloInicialNosPerifericos;
    
    /** Coordenada cartesiana do centro do grafo.
     */
    public Point2D.Double centro;
    
    /** Quantidade de n�s centrais (tamanho do anel central).
     */
    public ObservableInt numeroNosCentrais;
    
    /** Quantidade de n�s perif�ricos (tamanho do anel perif�rico).
     */
    public ObservableInt numeroNosPerifericos;
    
    /** N� que est� sendo movido no momento.
     */    
    private NoPolar noMovendoPolar;

    // Observa��o sobre implementa�ao do ArrayList "nos":
    // Nem sempre os n�s de um mesmo anel em "nos" est� ordenado.
    // Pode ocorrer situa��es como : 120 180 240 300 0 60.
    // Tomar cuidado com isso ao elaborar novos algoritmos.
    
    /** Cria uma nova inst�ncia de GrafoPolar.
     */
    public GrafoPolar() {
        super();
        corAreaPeriferica = new Color(0x009999);
        corAreaCentral = new Color(0xaba56a);
        corAreaProibida = new Color(0xCFa0a0);
        
        raioPeriferico = new ObservableInt(0);
        raioCentral = new ObservableInt(0);
        raioAreaProibida = new ObservableInt(0);
        
        anguloInicialNosCentrais = new ObservableAngle(90.0);
        anguloInicialNosPerifericos = new ObservableAngle(0.0);
        
        centro = new Point2D.Double(0,0);
        
        numeroNosCentrais = new ObservableInt(0);
        numeroNosPerifericos = new ObservableInt(0);
    }
    
    
    /**
     * Adiciona um n� ao grafo
     * Observa��o: Sobrescreve grafo.adicionarNo para atualizar n�mero de n�s centrais.
     * @param no N� a ser adicionado.
     * @param registrarModificacao Informa se a a��o de adicionar o n� deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    synchronized public void adicionarNo(No no, boolean registrarModificacao) {
        NoPolar noPolar = (NoPolar)no;
        if (!noPolar.eEscondido()) {
            if (noPolar.eCentral()) {
                numeroNosCentrais.add(1);
                //System.out.println("Novo n�mero de n�s centrais = "+numeroNosCentrais.getValue());
            } else {
                // � periferico
                numeroNosPerifericos.add(1);
                //System.out.println("Novo n�mero de n�s Perifericos = "+numeroNosPerifericos.getValue());
            }
        }
        super.adicionarNo(noPolar, registrarModificacao);
        //nos.add(no);
        reposicionarNos();
    }
    
    /**
     * Adiciona uma aresta entre os n�s no1 e no2, com peso e informa��o
     * associados. Se algum dos n�s informados n�o estiver no grafo, insere
     * esses n�s automaticamente.
     * Observa��es importantes:
     * - N�o verifica se a aresta ja existia antes da inser��o.
     * - Ela substitui Grafo.adicionarAresta para poder usar arestas da classe
     * ArestaPolar.
     * @param no1 Primeiro n� atingido pela aresta.
     * @param no2 Segundo n� atingido pela aresta.
     * @param peso Peso da aresta. Para arestas sem peso pode-se usar zero.
     * @param info Informa��o relacionada � aresta. Pode ser null.
     * @param registrarModificacao Informa se a a��o de adicionar a aresta deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    synchronized public void adicionarAresta(No no1, No no2, int peso, Info info, boolean registrarModificacao) {
        if (no1 instanceof NoPolar && no2 instanceof NoPolar) {
            ArestaPolar aresta = new ArestaPolar((NoPolar)no1, (NoPolar)no2, peso, info);
            adicionarAresta(aresta, registrarModificacao);
        }
    }
    
    /**
     * Adiciona ao grafo uma aresta informada.
     *  Observa��es
     *  - N�o verifica se essa aresta j� existe.
     *  - Caso um n� na extremidade da aresta n�o exista na lista de n�s,
     *  insere-o nessa lista.
     * @param aresta Aresta a ser adicionada ao grafo.
     * @param registrarModificacao Informa se a a��o de adicionar a aresta deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    synchronized public void adicionarAresta(Aresta aresta, boolean registrarModificacao) {
        if (aresta instanceof ArestaPolar) {
            super.adicionarAresta(aresta, registrarModificacao);
        }
    }
    
    /**
     * Remove um n� do grafo.
     * Observa��es:
     * - Sobrescreve grafo.removerNo para atualizar n�mero de n�s centrais.
     * - N�o verifica se n� existia em algum anel antes de fazer sua remo��o.
     * @param no N� a ser removido.
     * @param registrarModificacao Informa se a a��o de remover o n� deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    synchronized public void removerNo(No no, boolean registrarModificacao) {
        if (no instanceof NoPolar) {
            NoPolar noPolar = (NoPolar)no;
            if (!noPolar.eEscondido()) {
                if (noPolar.eCentral()) {
                    numeroNosCentrais.sub(1);
                } else {
                    // � periferico
                    numeroNosPerifericos.sub(1);
                }
            }
            super.removerNo(noPolar, registrarModificacao);
            reposicionarNos();
        }
    }
    
    /**
     * Esconde um n� do grafo.
     * @param no N� a ser escondido.
     * @param registrarModificacao Informa se a a��o de esconder o n� deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    public void esconderNo(No no, boolean registrarModificacao) {
        if (no instanceof NoPolar) {
            NoPolar noPolar = (NoPolar)no;
            if (noPolar.eMovendo()) {
                return; // � muito dificil para esconder n� que est� em movimento
            }
            
            // remover n� de lista em grafo
            int indexNo = nos.indexOf(noPolar);
            if (indexNo!=-1 && !no.eEscondido()) { // se n� pertence � lista de n�s, ent�o posso escond�-lo.
                if (noPolar.eCentral()) {
                    numeroNosCentrais.sub(1);
                } else {
                    // � periferico
                    numeroNosPerifericos.sub(1);
                }
                super.esconderNo(no, registrarModificacao);
//                if (noPolar.eMarcado()) {
//                    noPolar.desmarcar();
//                }
//                if (noPolar.eEscolhido()) {
//                    noPolar.desfazerEscolher();
//                }
//                noPolar.esconder();
            }
            reposicionarNos();
        }
    }
    
    /**
     * Torna vis�vel um n� que havia sido escondido.
     * @param no N� a ser mostrado.
     * @param registrarModificacao Informa se a a��o de mostrar o n� deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    public void mostrarNo(No no, boolean registrarModificacao) {
        if (no instanceof NoPolar) {
            NoPolar noPolar = (NoPolar)no;
            int indice=nos.indexOf(noPolar);
            if (indice!=-1 && no.eEscondido()) {
                if (noPolar.eCentral()) {
                    numeroNosCentrais.add(1);
                } else {
                    // � periferico
                    numeroNosPerifericos.add(1);
                }
                super.mostrarNo(noPolar, registrarModificacao);
                reposicionarNos();
            }
        }
    }
    
    /** Calcula a dist�ncia angular fixa existente entre os n�s de um dos dois an�is.
     * @param anel Anel a ser considerado.
     * @return Dist�ncia angular fixa entre os n�s do anel informado.
     */
    public double distanciaEntreNos(boolean anel) {
        double resposta;
        int tamanho;
        if (anel==NoPolar.ANEL_CENTRAL) {
            tamanho = numeroNosCentrais.getValue();
        } else {
            tamanho = numeroNosPerifericos.getValue();
        }
        if (tamanho > 0) {
            resposta = 360.0/tamanho;
        } else {
            resposta = 0;
        }
        return resposta;
    }
    
    /** Verifica qual o n� sob um determinado ponto (x,y).
     * @param x Abscissa do ponto.
     * @param y Ordenada do ponto.
     * @return  Retorna o n�, ou null se o n� n�o existir.
     */
    protected NoPolar noPolarEm(int x, int y) {
        No no = noEm(x,y);
        NoPolar nop;
        if (!(no instanceof NoPolar)) {
            nop = null;
        } else {
            nop = (NoPolar)no;
        }
        return nop;
    }
    
    
    /** Reposiciona todos os n�s eq�idistantemente uns dos outros em cada anel,
     * exceto noMovendo, que continua na mesma posi��o em que est�.
     */
    protected void reposicionarNos() {
        double distanciaNosCentrais    = distanciaEntreNos(ANEL_CENTRAL);
        double distanciaNosPerifericos = distanciaEntreNos(ANEL_PERIFERICO);
        
        double raioC = raioCentral.getValue();
        if (numeroNosCentrais.getValue() < 2) {
            raioC = 0.0;
        }
        
        // Calculando novos �ngulos para todos os n�s.
        // Todos os n�s centrais est�o ordenados por �ngulo entre si em "n�s".
        // Todos os n�s perif�ricos est�o ordenados por �ngulo entre si em "n�s".
        // O unico n� que n�o � ajustado � o noMovendoPolar.
        ListIterator li = nos.listIterator();
        int i = 0, j = 0;
        while (li.hasNext()) {
            NoPolar no = (NoPolar) li.next();
            if (!no.eEscondido()) {
                if (no.eCentral()) {
                    if (no != noMovendoPolar) {
                        no.raio   = raioC;
                        no.angulo = (anguloInicialNosCentrais.getValue() + (i*distanciaNosCentrais)) % 360.0;
                        if (no.angulo < 0) {
                            no.angulo += 360.0;
                        }
                    }
                    i++;
                } else {
                    if (no != noMovendoPolar) {
                        no.raio   = raioPeriferico.getValue();
                        no.angulo = (anguloInicialNosPerifericos.getValue() + (j*distanciaNosPerifericos)) % 360.0;
                        if (no.angulo < 0) {
                            no.angulo += 360.0;
                        }
                    }
                    j++;
                }
            }
        }
        atualizarHashArestas();
    }
    
    /** Atualiza HashMap de arestas, que � usado no algoritmo de sele��o de
     * arestas.
     */
    private void atualizarHashArestas() {
        arestasHash.clear();
        Iterator it = arestas.iterator();
        while(it.hasNext()) {
            ArestaPolar a = (ArestaPolar)it.next();
            arestasHash.put(a.retornarChave(), a);
        }
    }
    
    /** Move um n� para o anel perif�rico.
     * Observa��o: N�o verifica se o n� pertence � lista de n�s do grafo.
     * @param no N� a ser movido.
     */
    public void moverParaAnelPeriferico(NoPolar no) {
        if (no.eCentral()) {
            girarAnelParaEfetuarRemocao(no, NoPolar.ANEL_CENTRAL);
            posicionarCorretamenteNaListaDeNos(no, NoPolar.ANEL_PERIFERICO);
            no.moverParaAnelPeriferico();
            numeroNosCentrais.sub(1);
            numeroNosPerifericos.add(1);
            girarAnelParaEfetuarInsercao(no, NoPolar.ANEL_PERIFERICO);
            reposicionarNos();
        }
    }
    
    /** Move um n� para o anel central.
     * Observa��o: N�o verifica se o n� pertence � lista de n�s do grafo.
     * @param no No a ser movido.
     */
    public void moverParaAnelCentral(NoPolar no) {
        if (no.ePeriferico()) {
            girarAnelParaEfetuarRemocao(no, NoPolar.ANEL_PERIFERICO);
            posicionarCorretamenteNaListaDeNos(no, NoPolar.ANEL_CENTRAL);
            no.moverParaAnelCentral();
            numeroNosCentrais.add(1);
            numeroNosPerifericos.sub(1);
            girarAnelParaEfetuarInsercao(no, NoPolar.ANEL_CENTRAL);
            reposicionarNos();
        }
    }
    
    
    /** Modifica o �ngulo inicial do anel para o qual um n� ser� movido, de 
     * modo que o n� a ser inserido esteja em uma posi��o angular equidistante
     * de seus n�s vizinhos. Ou seja, faz com que o buraco que vai ser aberto
     * para este n� no anel tenha exatamente o mesmo �ngulo que o n� no momento
     * da inser��o.
     * @param no N� a ser inserido.
     * @param anel Anel a ser girado.
     */    
    public void girarAnelParaEfetuarInsercao(NoPolar no, boolean anel) {
        double anguloInsercao = no.angulo;
        double anguloInicial, novoAnguloInicial, delta;
        if (anel == NoPolar.ANEL_CENTRAL) {
            anguloInicial=anguloInicialNosCentrais.getValue();
        } else {
            anguloInicial=anguloInicialNosPerifericos.getValue();
        }
        novoAnguloInicial = anguloInsercao - (retornarPosicaoNoAnel(no)) * distanciaEntreNos(anel);
        // Linha acima pode estar com problema. retornarPosicaoNoAnel n�o faz o que se pensa que ela faz.
        delta = novoAnguloInicial - anguloInicial;
        
        girarAnel(anel, delta);
    }

    /** Retorna posi��o do n� informado em seu anel, considerando como posi��o zero o
     * n� que ocupa (ou que deveria estar ocupando) a posi��o indicada pelo �ngulo
     * inicial do n�.
     * @param no N� cuja posi��o deve ser calculada.
     * @return Posi��o do n� no anel, como explicado anteriormente.
     */    
    private int retornarPosicaoEfetivaNoAnel(NoPolar no) {
        // Retorna posicao no anel, tendo por base o n� que tem angulo inicial.
        NoPolar aux;
        double anguloInicial;
        int posInicial = -1;
        int posNo = -1;
        int posRealNo = -1;
        if (no.retornarAnel() == NoPolar.ANEL_CENTRAL) {
            anguloInicial=anguloInicialNosCentrais.getValue();
        } else {
            anguloInicial=anguloInicialNosPerifericos.getValue();
        }
        for (int i=0; i<nos.size(); i++) {
            aux = (NoPolar)nos.get(i);
            if (aux.retornarAnel() == no.retornarAnel() && !aux.eEscondido()) {
                if (Math.abs(aux.angulo - anguloInicial)<0.01) { // praticamente iguais
                    posInicial = i;
                }
                if (Math.abs(aux.angulo - no.angulo)<0.01) { // praticamente iguais
                    posNo = i;
                }
            }
        }
        if (posInicial == -1 && nos.contains(no)) {
            posInicial = posNo;
        }
        if (posInicial != -1 && posNo != -1) {
            if (posNo >= posInicial) {
                posRealNo = posNo - posInicial;
            } else {
                posRealNo = nos.size() - posInicial + posNo;
            }
        }
        return posRealNo;
    }
    
    /** Modifica o �ngulo inicial do anel do qual um n� foi movido, de modo a 
     * parecer que ambos os ex-vizinhos desse n� caminharam a mesma dist�ncia 
     * angular durante o reposicionamento dos n�s no anel. Dessa forma, os
     * n/2 n�s que est�o � direita do buraco se comportam da mesma forma que os
     * n/2 n�s restantes no reposicionamento: os que estavam mais pertos do 
     * buraco parecem ter andado mais, e os que estavam mais longe parecem ter
     * andado menos.
     * @param no N� a ser removido do anel.
     * @param anel Anel do qual o n� ser� removido.
     */    
    public void girarAnelParaEfetuarRemocao(NoPolar no, boolean anel) {
        double anguloInicial, novoAnguloInicial, delta;
        if (anel == NoPolar.ANEL_CENTRAL) {
            anguloInicial=anguloInicialNosCentrais.getValue();
        } else {
            anguloInicial=anguloInicialNosPerifericos.getValue();
        }

        if (retornarTamanhoAnel(anel)>2) {
            double k = retornarPosicaoEfetivaNoAnel(no);
            double anguloBuraco = anguloInicial + k * distanciaEntreNos(anel);
            double novaDistanciaEntreNos = 360 / ((double)retornarTamanhoAnel(anel) - 1);
            novoAnguloInicial = Util.normalizarAngulo(anguloBuraco - novaDistanciaEntreNos * ( k - 0.5 ));
            delta = novoAnguloInicial - anguloInicial;
            girarAnel(anel, delta);
        }
        
        
    }
    
    
    /** Verifica qual a posi��o correta que o n� informado deve ocupar em nos.
     * Se estiver na posi��o errada, insere-o na posi��o correta.
     * @param no N� a ser posicionado.
     * @param anel Anel em que o n� ser� posicionado.
     */
    public void posicionarCorretamenteNaListaDeNos(NoPolar no, boolean anel) {
        int tamanhoAnel;
        double anguloInicial;
        
        
        if (anel == NoPolar.ANEL_CENTRAL) {
            tamanhoAnel = numeroNosCentrais.getValue();
            anguloInicial=anguloInicialNosCentrais.getValue();
        } else {
            tamanhoAnel = numeroNosPerifericos.getValue();
            anguloInicial=anguloInicialNosPerifericos.getValue();
        }
        
        
        if (tamanhoAnel >0) {
            nos.remove(no);
            NoPolar noAnterior = null;
            NoPolar noSeguinte = null;
            int i = -1;
            do {
                i++;
                noAnterior = (NoPolar)nos.get(i);
            } while (noAnterior.retornarAnel()!=anel);
            // noAnterior tem agora o primeiro n� do anel.
            boolean acheiPosInsercao = false;
            //i--;
            double aAnterior, aSeguinte;
            while (!acheiPosInsercao) {
                do {
                    i=(i+1) % nos.size();
                    noSeguinte = (NoPolar)nos.get(i);
                } while (noSeguinte.retornarAnel()!=anel);
                aAnterior = noAnterior.angulo % 360;
                aSeguinte = noSeguinte.angulo % 360;
                if (aSeguinte<aAnterior) { aSeguinte+=360; }
                acheiPosInsercao = ((aAnterior <= no.angulo) && (no.angulo < aSeguinte)) ||
                ((aAnterior <= no.angulo+360) && (no.angulo + 360 < aSeguinte)) ||
                (aAnterior == aSeguinte);
                if (aAnterior == aSeguinte) {
                    if (PolarCartesiano.menorDistanciaEntreAngulos(no.angulo, anguloInicial) < 90) {
                        i=0;
                    } else {
                        i=nos.size();
                    }
                }
                
                if (!acheiPosInsercao) { noAnterior = noSeguinte; }
            }
            // achei posicao de insercao ==> antes de noSeguinte.
            
            nos.add(i,no);
            
        } // else {
            //Anel so tem 1 elemento. N�o precisa posicionar.
        //}
        
    }
    
    /** Ajusta o raio perif�rico para um valor espec�fico.
     * @param novoRaio Novo raio perif�rico.
     */
    public void ajustarRaioPeriferico(int novoRaio) {
        if (novoRaio > 0) {
            raioPeriferico.setValue(novoRaio);
            reposicionarNos();
        }
    }
    
    /** Ajusta o raio central para um valor espec�fico.
     * @param novoRaio Novo raio central.
     */
    public void ajustarRaioCentral(int novoRaio) {
        if (novoRaio > 0) {
            raioCentral.setValue(novoRaio);
            reposicionarNos();
        }
    }
    
    /** Ajusta o raio da �rea proibida para um valor espec�fico.
     * @param novoRaio Novo raio da �rea proibida.
     */
    public void ajustarRaioAreaProibida(int novoRaio) {
        if (novoRaio > 0) {
            raioAreaProibida.setValue(novoRaio);
        }
    }
    
    /** Retorna o valor do raio perif�rico.
     * @return Valor do raio perif�rico.
     */
    public double retornarRaioPeriferico() {
        return raioPeriferico.getValue();
    }
    
    /** Retorna o valor do raio central.
     * @return Valor do raio central.
     */
    public double retornarRaioCentral() {
        return raioCentral.getValue();
    }
    
    /** Retorna o valor do raio da �rea proibida.
     * @return Valor do raio da �rea proibida.
     */
    public double retornarRaioAreaProibida() {
        return raioAreaProibida.getValue();
    }
    
    /** Gira o anel especificado, adicionando ao �ngulo inicial do anel o
     * �ngulo informado.
     * @param anel Anel a ser rotacionado.
     * @param angulo Valor a ser adicionado aos �ngulos dos n�s.
     */
    public void girarAnel(boolean anel, double angulo) {
        if (anel==NoPolar.ANEL_CENTRAL) {
            anguloInicialNosCentrais.setValue(anguloInicialNosCentrais.getValue() + angulo);
        } else if (anel==NoPolar.ANEL_PERIFERICO) {
            anguloInicialNosPerifericos.setValue(anguloInicialNosPerifericos.getValue() + angulo);
        }
        reposicionarNos();
    }
    
    /** Testa se o ponto (x,y) se encontra no ret�ngulo em que o grafo � desenhado.
     * @param x Abscissa do ponto.
     * @param y Ordenada do ponto.
     * @return  Retorna AREA_DE_NO - se o ponto est� sobre um n�;
     *          AREA_INTERIOR - se est� dentro do ret�ngulo mas fora de um n�;
     *          AREA_EXTERIOR - se est� fora do ret�ngulo.
     */
    
/*    public int areaEm(int x, int y) {
        int tx = (int) (centro.x - largura / 2);
        int ty = (int) (centro.y - altura / 2);
        
        if  (x >= tx && y>= ty && (x-tx) < largura && (y-ty) < altura) {
            ListIterator li = nos.listIterator();
            while (li.hasNext()) {
                NoPolar no = (NoPolar) li.next();
                if (no.areaEm(x,y) == NoPolar.AREA_INTERIOR && !no.eEscondido()) return AREA_DE_NO;
            }
            return AREA_INTERIOR;
        }
        return AREA_EXTERIOR;
    }
  */  
    
    /** Registra qual n� est� em movimento.
     * @param no N� que est� em movimento.
     */
    public void registrarNoMovendo(No no) {
        super.registrarNoMovendo(no);
        noMovendoPolar = (NoPolar)no;
    }
    
        /** Desfaz o registro de qual n� est� em movimento.
     */
    public void desfazerRegistrarNoMovendo() {
        super.desfazerRegistrarNoMovendo();
        noMovendoPolar = null;
    }
    
    /** Move o n� que est� em movimento para a posi��o correta do anel ao qual
     * ele pertence.
     */
    public void posicionarCorretamenteNoMovendo() {
        if (noMovendoPolar != null) {
            NoPolar no = noMovendoPolar;  // Normalmente reposicionarNos nao muda
            noMovendoPolar = null;        // posicao de noMovendo, por isso � preciso
            reposicionarNos();       // "desfazer" noMovendo para executar
            noMovendoPolar = no;          // essa fun��o
        }
    }
    
    /** Mover n� que est� em movimento para a posi��o nova (x,y).
     * @param x Abscissa da nova posi��o.
     * @param y Ordenada da nova posi��o.
     */
    public void moverNoMovendo(int x, int y) {
        boolean troqueiDeAnel = false;
        Dimension d = retornarDimensaoDaTela();
        // ajusta para que o n� n�o possa ser movido para fora do ret�ngulo do
        // grafo
        int tx = (int) (centro.x - d.width/ 2);  // canto superior do ret�ngulo
        int ty = (int) (centro.y - d.height / 2);   // que enquadra o grafo.
        int nx = x, ny = y; // posi��o do mouse
        if (x < tx) {
            nx = tx;
        }
        if (x > (tx + d.width)) {
            nx = tx + (int) d.width;
        }
        if (y < ty) {
            ny = ty;
        }
        if (y > (ty + d.height)) {
            ny = ty + (int) d.height;
        }
        // aqui nx e ny j� tem a posi��o do mouse, ajustada para estar dentro
        // do ret�ngulo
        
        // ajustando posi��o do noMovendo
        noMovendoPolar.raio = PolarCartesiano.cartesiano2Polar_raio(nx,ny,centro);
        noMovendoPolar.angulo = PolarCartesiano.cartesiano2Polar_angulo(nx,ny,centro);
        noMovendoPolar.x = nx;
        noMovendoPolar.y = ny;
        
        double distOrig = distanciaEntreNos(noMovendoPolar.retornarAnel());
        double angulo = noMovendoPolar.angulo;
        
        // Comparar raio de noMovendo com raio central e perif�rico, para
        // descobrir se � necess�rio mover o n� para outro anel
        double rnp = Math.abs(noMovendoPolar.raio - raioPeriferico.getValue());
        double rnc = Math.abs(noMovendoPolar.raio - raioCentral.getValue());
        if (rnp < rnc && noMovendoPolar.eCentral()) { // mover para anel periferico
            moverParaAnelPeriferico(noMovendoPolar);
            troqueiDeAnel = true;
        } else if (rnp > rnc && noMovendoPolar.ePeriferico()) { // mover para anel central
            moverParaAnelCentral(noMovendoPolar);
            troqueiDeAnel = true;
        }
        
        if (!troqueiDeAnel) {
            double anguloInicial;
            int tamanhoAnel;
            if (noMovendoPolar.retornarAnel()==NoPolar.ANEL_CENTRAL) {
                anguloInicial=anguloInicialNosCentrais.getValue();
                tamanhoAnel=numeroNosCentrais.getValue();
            } else {
                anguloInicial=anguloInicialNosPerifericos.getValue();
                tamanhoAnel=numeroNosPerifericos.getValue();
            }
            if (tamanhoAnel>1) {
                int posNoMovendo = retornarPosicaoNoAnel(noMovendoPolar);
                double anguloCorreto = posNoMovendo * distOrig + anguloInicial;
                anguloCorreto = Util.normalizarAngulo(anguloCorreto);
                if (PolarCartesiano.menorDistanciaEntreAngulos(angulo, anguloCorreto) > distOrig) {
                    double a1 = Math.floor((angulo-anguloInicial) / distOrig) * distOrig + anguloInicial;
                    double a2 = Math.ceil((angulo-anguloInicial) / distOrig) * distOrig + anguloInicial;
                    a1=Util.normalizarAngulo(a1);
                    a2=Util.normalizarAngulo(a2);
                    NoPolar no = null;
                    if (PolarCartesiano.menorDistanciaEntreAngulos(angulo, a1)<PolarCartesiano.menorDistanciaEntreAngulos(angulo,a2)) {
                        // entao noMovendo deve ser trocado de posicao
                        // com o no de angulo a1.
                        no = retornarNo(a1,noMovendoPolar.retornarAnel());
                        
                    } else {
                        // senao noMovendo deve ser trocado de posicao
                        // com o no de angulo a2
                        no = retornarNo(a2,noMovendoPolar.retornarAnel());
                    }
                    // trocarNos(noMovendo, no);  // Pode ser que n� e n�Movendo nao sejam vizinhos.
                    // Por isso precisei fazer uma rotina de rota��o de um peda�o de lista.
                    rotacionarSecaoAnel(noMovendoPolar, no); // n�Movendo deve entrar no lugar de n�.
                    reposicionarNos();
                } // else we need to do anything.
            } //else {
            // Nao preciso fazer nada se anel tiver apenas 1 elemento.
            //}
            
            
            
        }
        
    }

    /** Rotaciona os elementos da lista de an�is entre um intervalo de n�s especificado.
     * @param noASerReposicionado N� que � retirado de uma extremidade do intervalo para ser posicionado na outra.
     * @param noDestino A outra extremidade do intervalo.
     */    
    private void rotacionarSecaoAnel(NoPolar noASerReposicionado, NoPolar noDestino) {
        boolean anel = noASerReposicionado.retornarAnel();
        if (anel == noDestino.retornarAnel()) {
            // noDestino vai ceder lugar ao noASerReposicionado, fazendo um "shift" no array.
            int iRealDestino = nos.indexOf(noDestino);
            int iRealRepos = nos.indexOf(noASerReposicionado);
            int iDestino = retornarPosicaoNoAnel(noDestino);
            int iRepos = retornarPosicaoNoAnel(noASerReposicionado);
            if (iDestino == retornarTamanhoAnel(anel)-1 && iRepos == 0) {
                nos.remove(noASerReposicionado);
                nos.remove(noDestino);
                nos.add(0, noDestino);
                nos.add(iRealDestino, noASerReposicionado);
            } else if (iRepos==retornarTamanhoAnel(anel)-1 && iDestino == 0) {
                nos.remove(noASerReposicionado);
                nos.remove(noDestino);
                nos.add(0, noASerReposicionado);
                nos.add(iRealRepos, noDestino);
            } else {
               /* Para efetuar a rota��o, primeiramente um n� � retirado; em seguida, os demais
                * n�s do intervalo se movem na lista na dire��o do vazio deixado pelo n� retirado;
                * por fim o n� retirado � recolocado no novo espa�o vazio que aparece na lista
                * ap�s o deslocamento de todos os elementos do intervalo.
                */
                nos.remove(noASerReposicionado);
                nos.add(iRealDestino, noASerReposicionado);
            }
        }
        /* Este m�todo ainda n�o est� perfeito. Pode vir a dar problemas no futuro. (Celmar - 02/12/2003) */
    }

    
    /** Retorna o n� que apresenta o �ngulo informado em um anel determinado.
     * Observa��o: Precis�o do �ngulo = 0.01
     * @param angulo �ngulo do n� a ser procurado.
     * @param anel Anel em que o n� deve ser procurado.
     * @return Retorna o n�, se existir, ou null caso contr�rio.
     */
    private NoPolar retornarNo(double angulo, boolean anel) {
        ListIterator li = nos.listIterator();
        double a1= Math.rint(angulo*100)/100;
        boolean achei = false;
        NoPolar no = null;
        while (li.hasNext() && !achei) {
            no = (NoPolar)li.next();
            achei = (no.retornarAnel() == anel && Math.rint(no.angulo*100)/100 == a1 && !no.eEscondido());
        }
        if (!achei) {
            no = null;
        }
        return no;
    }
    
    /** Retorna a posi��o de um n� na lista de nos do programa,
     * com rela��o aos n�s do anel ao qual pertence.
     * Pode acontecer de os n�s no in�cio da lista n�o serem os n�s de menor �ngulo.
     * A primeira posi��o � zero.
     * Ignora n�s escondidos.
     * @param no N� a ser procurado.
     * @return Posi��o do n�, com rela��o aos demais n�s de seu anel, na lista de n�s "nos".
     */
    private int retornarPosicaoNoAnel(NoPolar no) {
        ListIterator li = nos.listIterator();
        int i = -1;
        boolean achei = false;
        NoPolar no2;
        while (li.hasNext() && !achei) {
            no2 = (NoPolar)li.next();
            if (no2.retornarAnel() == no.retornarAnel() && !no2.eEscondido()) {
                i++;
                achei = (no == no2);
            }
        }
        return i;
        
    }
    
//    /** Troca dois n�s de posi��o na lista de n�s.
//     * @param no1 Um dos n�s a ser trocado de posi��o.
//     * @param no2 O outro n� a ser trocado de posi��o.
//     */
//    private void trocarNos(NoPolar no1, NoPolar no2) {
//        int i1 = nos.indexOf(no1);
//        int i2 = nos.indexOf(no2);
//        if (i1>=0 && i2>=0) {
//            nos.set(i1,no2);
//            nos.set(i2,no1);
//        } // else { ... um dos n�s n�o existe na lista de n�s do grafo. N�o fazer troca. }
//    }
    
//    /** Informa qual n� vem antes do n� informado, considerando a seq��ncia de n�s do
//     * anel ao qual ele pertence.
//     * @param no N� cujo n� anterior deve ser buscado.
//     * @return N� anterior ao n� informado. Se houver apenas 1 n� no anel, retorna o pr�prio
//     * n�. Se falhar, retorna null.
//     */
//    private NoPolar noAnterior(NoPolar no) {
//        NoPolar anterior = null;
//        switch (retornarTamanhoAnel(no.retornarAnel())) {
//            case 0:
//                anterior = null; // o que seria estranho, porque pelo menos "no" deve estar no anel.
//                break;
//            case 1:
//                if (!no.eEscondido()) {
//                    anterior = no; // se s� h� 1 n� no anel, entao o anterior � o pr�prio "no"
//                }
//                break;
//            default:
//                int i = nos.indexOf(no);
//                // Corrigir. Pode acontecer em casos extremos que trave o programa.
//                int limite = nos.size();
//                do {
//                    i = (nos.size() + i - 1) % nos.size();
//                    anterior = (NoPolar)nos.get(i);
//                    limite--;
//                } while ((anterior.retornarAnel() != no.retornarAnel() || (anterior.eEscondido()) ) && limite > 0);
//                if (anterior.retornarAnel() != no.retornarAnel() || (anterior.eEscondido()) ) {
//                    anterior = null;
//                }
//        }
//        
//        return anterior;
//    }
    
//    /** Informa qual n� vem ap�s o n� informado, considerando a seq��ncia de n�s do anel
//     * ao qual ele pertence.
//     * @param no N� cujo n� posterior deve ser buscado.
//     * @return N� posterior ao n� informado. Se houver apenas 1 n� no anel, retorna o pr�prio
//     * n�. Se falhar, retorna null.
//     */
//    private NoPolar noPosterior(NoPolar no) {
//        NoPolar posterior = null;
//        switch (retornarTamanhoAnel(no.retornarAnel())) {
//            case 0:
//                posterior = null; // o que seria estranho, porque pelo menos "no" deve estar no anel.
//                break;
//            case 1:
//                posterior = no; // se s� h� 1 n� no anel, entao o posterior � o pr�prio "no"
//                break;
//            default:
//                int i = nos.indexOf(no);
//                int limite = nos.size();
//                do {
//                    i = (i + 1) % nos.size();
//                    posterior = (NoPolar)nos.get(i);
//                    limite --;
//                } while ((posterior.retornarAnel() != no.retornarAnel() || posterior.eEscondido()) && limite>0);
//                if (posterior.retornarAnel() != no.retornarAnel() || posterior.eEscondido()) {
//                    posterior = null;
//                }
//        }
//        return posterior;
//    }
    
    
    /** Informa o tamanho do anel especificado.
     * @param anel Anel cujo tamanho deve ser retornado..
     * @return Tamanho do anel.
     */
    private int retornarTamanhoAnel(boolean anel) {
        int tamanhoAnel;
        if (anel==NoPolar.ANEL_CENTRAL) {
            tamanhoAnel=numeroNosCentrais.getValue();
        } else {
            tamanhoAnel=numeroNosPerifericos.getValue();
        }
        return tamanhoAnel;
    }
    
    // M�todos para sele��o de arestas retas
    
    /* Dado um n�, um ponto M=(mx,my) e um anel, descobre a linha que passaria
     * por esses tr�s elementos. A seguir, descobre o �ngulo alfa do n� mais
     * pr�ximo dessa linha no anel informado. 0<=alfa<360
     */
    /** Dado um n� noA, um ponto M=(xm,ym) e um anel A do grafo polar, verifica qual o
     * �ngulo do ponto em A atingido pela reta que passa por M e noA.
     * @param noA N� pelo qual passa a reta.
     * @param xm Abscissa do ponto pelo qual passa a reta.
     * @param ym Ordenada do ponto pelo qual passa a reta.
     * @param anel Anel atingido pela reta.
     * @return �ngulo do ponto atingido pela reta no anel especificado.
     */
    public double anguloAtingidoPelaRetaNoAnel(NoPolar noA, int xm, int ym, boolean anel) {
        
        double fiB1, fiB2, fiB, fiM, a, c, d1, d2, rm, rb, dam, xa, ya,
        dmb1, dab1, xb1, yb1, dmb2, dab2, xb2, yb2, anguloEscolhido, xb, yb ;
        double resposta = -1;
        
        if (noA !=null) {
            rm = PolarCartesiano.cartesiano2Polar_raio(xm, ym, centro);
            fiM = PolarCartesiano.cartesiano2Polar_angulo(xm, ym, centro);
            if (rm <= Math.max(raioPeriferico.getValue(), raioCentral.getValue())) {
                if (anel == NoPolar.ANEL_CENTRAL) {
                    if (numeroNosCentrais.getValue() == 1) {
                        rb = 0;
                    } else {
                        rb = raioCentral.getValue();
                    }
                } else {
                    rb = raioPeriferico.getValue();
                }
                
                if (rb==0) {
                    anguloEscolhido = 0; /// caso n�o tratado.
                    xb = centro.x;
                    yb = centro.y;
                    xa = PolarCartesiano.polar2Cartesiano_x(noA.raio, noA.angulo, centro);
                    ya = PolarCartesiano.polar2Cartesiano_y(noA.raio, noA.angulo, centro);
                    
                } else {
                    
                    if (noA.eCentral() && numeroNosCentrais.getValue()==1) {
                        fiB = fiM;
                        xa = centro.x;
                        ya = centro.y;
                    } else {
                        a = fiM - noA.angulo;
                        xa = PolarCartesiano.polar2Cartesiano_x(noA.raio, noA.angulo, centro);
                        ya = PolarCartesiano.polar2Cartesiano_y(noA.raio, noA.angulo, centro);
                        
                        dam =  PolarCartesiano.distanciaEntrePontos(xa,ya,xm,ym);
                        c = Math.toDegrees(Math.asin(rm / dam * Math.sin(Math.toRadians(a) ) ) );
                        
                        if (noA.raio - rm * Math.cos(Math.toRadians(a)) < 0) {
                            c = 180 - c;
                        }
                        
                        d1 = Math.toDegrees(Math.asin(rm / rb * Math.sin(Math.toRadians(a + c))));
                        d2 = 180 - d1;
                        fiB1 = 180 - c - d1 + noA.angulo;
                        fiB2 = 180 - c - d2 + noA.angulo;
                        
                        xb1 = PolarCartesiano.polar2Cartesiano_x(rb, fiB1, centro);
                        yb1 = PolarCartesiano.polar2Cartesiano_y(rb, fiB1, centro);
                        dmb1 = PolarCartesiano.distanciaEntrePontos(xm,ym,xb1,yb1);
                        dab1 = PolarCartesiano.distanciaEntrePontos(xa,ya,xb1,yb1);
                        xb2 = PolarCartesiano.polar2Cartesiano_x(rb, fiB2, centro);
                        yb2 = PolarCartesiano.polar2Cartesiano_y(rb, fiB2, centro);
                        dmb2 = PolarCartesiano.distanciaEntrePontos(xm,ym,xb2,yb2);
                        dab2 = PolarCartesiano.distanciaEntrePontos(xa,ya,xb2,yb2);
                        
                        // Teoricamente, dam + dmb1 - dab1 = 0 OU dam + dmb2 - dab2 = 0
                        // No entanto, isso n�o est� acontecendo por problemas com precis�o.
                        // Assim, para efetuar a escolha entre 1 ou 2, quem tiver o menor erro ganha.
                        if (dam + dmb1 - dab1 < dam + dmb2 - dab2) {
                            fiB = fiB1;
                        } else {
                            fiB = fiB2;
                        }
                    }
                    
                    double[] v;
                    v = retornarAngulosVizinhos(fiB, anel);
                    
                    double dv0,dv1;
                    dv0=PolarCartesiano.menorDistanciaEntreAngulos(fiB,v[0]);
                    dv1=PolarCartesiano.menorDistanciaEntreAngulos(fiB,v[1]);
                    
                    if (dv0<=dv1) {
                        anguloEscolhido=v[0];
                    } else {
                        anguloEscolhido=v[1];
                    }
                    
                }
                // Mouse deve estar suficientemente pr�ximo da aresta em quest�o.
                xb = PolarCartesiano.polar2Cartesiano_x(rb, anguloEscolhido, centro);
                yb = PolarCartesiano.polar2Cartesiano_y(rb, anguloEscolhido, centro);
                
                
                boolean existe = pontoEstaProximoDaAresta(xm,ym,xa,ya,xb,yb) &&
                (anguloEscolhido!=noA.angulo || noA.retornarAnel() != anel);
                
                if (existe) {
                    resposta = anguloEscolhido;
                }
            }
        }
        
        return(resposta);
    }
    
    
    
    /** Dado um �ngulo e um anel, retorna quais s�o os �ngulos dos n�s
     * imediatamente vizinhos deste �ngulo no anel.
     * @param angulo �ngulo do qual se deve descobrir os vizinhos.
     * @param anel Anel no qual se deve descobrir os �ngulos vizinhos.
     * @return array contendo os dois �ngulos vizinhos ao �ngulo informado.
     */
    public double[] retornarAngulosVizinhos(double angulo, boolean anel) {
        
        double anguloInicial;
        double distancia;
        
        double[] r = new double[2];
        
        double anguloNormalizado = Util.normalizarAngulo(angulo);
        
        if (anel==NoPolar.ANEL_CENTRAL) {
            anguloInicial=anguloInicialNosCentrais.getValue();
        } else {
            anguloInicial=anguloInicialNosPerifericos.getValue();
        }
        distancia=distanciaEntreNos(anel);
        
        int i;

        i = (int)((anguloNormalizado-anguloInicial) / distancia);
        r[0] = i * distancia + anguloInicial;
        r[1] = r[0] + distancia;
        r[0] = Util.normalizarAngulo(r[0]);
        r[1] = Util.normalizarAngulo(r[1]);
        return (r);
    }
    

    
    
    
    /** Dado um ponto M=(mx,my), calcula quais seriam as arestas de um grafo polar
     * completo que come�ariam no anel 1, passariam por esse ponto e atingiriam
     * o anel 2. Como nem todo grafo � completo, as arestas retornadas formam um
     * conjunto de arestas que possivelmente existem no grafo atual.
     * @param mx Abscissa de M.
     * @param my Ordenada de M.
     * @param anel1 Anel origem.
     * @param anel2 Anel destino.
     * @return Retorna um conjunto de chaves de poss�veis arestas que sairiam de 
     * anel1, passariam por M e chegariam a anel2.
     */
    public Set retornarAngulosDePossiveisArestasEm(int mx, int my, boolean anel1, boolean anel2) {
        // anel1 - anel que ser� percorrido.
        // anel2 - anel onde o n�-destino ser� procurado
        // mx,my - mouse
        
        NoPolar n;
        Set s = new HashSet();
        
        //ParDeAngulos p;
        ChaveDeAresta ch;
        
        double angulo;
        for (int i = 0; i<nos.size(); i++) {
            n = (NoPolar)nos.get(i);
            if (n.retornarAnel() == anel1 && !n.eEscondido()) {
                angulo = anguloAtingidoPelaRetaNoAnel(n, mx, my, anel2);
                if (angulo != -1) {
                    //p = new ParDeAngulos(n.angulo, angulo);
                    ch = new ChaveDeAresta(n.angulo, anel1, angulo, anel2);
                    //s.add(p);
                    s.add(ch);
                }
            }
        }
        return (s);
        
    }
    
    
    /** Algoritmo linear ( O(n), n = n�mero de n�s ) para descobrir o conjunto de
     * arestas que passam por um ponto M=(xm,ym) no grafo polar.
     * @param xm Abscissa de M.
     * @param ym Ordenada de M.
     * @return Retorna o conjunto de arestas do grafo que passam pelo ponto M.
     */
    public Set retornarConjuntoArestasEmLinear(int xm, int ym) {
        Set s = new HashSet();
        Set conjuntoArestas = new HashSet();
        ArestaPolar a;
        ChaveDeAresta ch1, ch2;
        boolean anel1, anel2;
        
        for (int i=0; i<=3; i++) {
            switch (i) {
                case 0:
                    anel1 = NoPolar.ANEL_PERIFERICO;
                    anel2 = NoPolar.ANEL_PERIFERICO;
                    break;
                case 1:
                    anel1 = NoPolar.ANEL_CENTRAL;
                    anel2 = NoPolar.ANEL_PERIFERICO;
                    break;
                case 2:
                    anel1 = NoPolar.ANEL_CENTRAL;
                    anel2 = NoPolar.ANEL_CENTRAL;
                    break;
                case 3: // talvez desnecessario
                    anel1 = NoPolar.ANEL_PERIFERICO;
                    anel2 = NoPolar.ANEL_CENTRAL;
                    break;
                default:
                    anel1 = NoPolar.ANEL_PERIFERICO;
                    anel2 = NoPolar.ANEL_PERIFERICO;
            }
            s = retornarAngulosDePossiveisArestasEm(xm, ym, anel1, anel2);
            
            // Obs: Talvez possa ser simplificado... uma chave somente ja bastaria...
            for (Iterator si = s.iterator(); si.hasNext(); ) {
                ch1 = (ChaveDeAresta)si.next();
                ch2 = new ChaveDeAresta(ch1.angulo2, ch1.anel2, ch1.angulo1, ch1.anel1);
                a = (ArestaPolar)arestasHash.get(ch1);
                if (a!=null) {
                    if (a.eAtingida() && !a.eEscondida()) {
                        conjuntoArestas.add(a);
                    }
                } else {
                    a = (ArestaPolar)arestasHash.get(ch2);
                    if (a!=null) {
                        if (a.eAtingida() && !a.eEscondida()) {
                            conjuntoArestas.add(a);
                        }
                    }
                }
            }
        }
        conjuntoArestas.addAll(retornarConjuntoAutoArestasEm(xm,ym));
        return(conjuntoArestas);
    }
    
    /** Algoritmo quadr�tico ( O(n^2), n = n�mero de n�s ) para descobrir o conjunto de
     * arestas que passam por um ponto M=(xm,ym) no grafo.
     * @param xm Abscissa de M.
     * @param ym Ordenada de M.
     * @return Retorna o conjunto de arestas do grafo que passam pelo ponto M.
     */
    private Collection retornarConjuntoArestasEmQuadratico(int xm, int ym) {
        double xa,xb,ya,yb;
        HashSet conjuntoArestas = new HashSet();
        ArestaPolar a;
        NoPolar no1, no2;
        for (Iterator i = arestas.iterator(); i.hasNext();) {
            a = (ArestaPolar)i.next();
            if (!a.eEscondida()) {
                no1 = (NoPolar)(a.no1);
                no2 = (NoPolar)(a.no2);
                xa = PolarCartesiano.polar2Cartesiano_x(no1.raio, no1.angulo, centro);
                ya = PolarCartesiano.polar2Cartesiano_y(no1.raio, no1.angulo, centro);
                xb = PolarCartesiano.polar2Cartesiano_x(no2.raio, no2.angulo, centro);
                yb = PolarCartesiano.polar2Cartesiano_y(no2.raio, no2.angulo, centro);
                if (a.eAtingida() && pontoEstaProximoDaAresta(xm,ym,xa,ya,xb,yb)) {
                    conjuntoArestas.add(a);
                }
            }
        }
        return (conjuntoArestas);
    }
    
    /** Algoritmo linear (O(n), n = n�mero de n�s) para descobrir o conjunto de
     * auto-arestas (arestas cujas extremidades incidem sobre o mesmo n�) que passam
     * pelo ponto M=(xm,ym).
     * @param xm Abscissa de M.
     * @param ym Ordenada de M.
     * @return Retorna o conjunto de auto-arestas do grafo que passam pelo ponto M.
     */
    public Collection retornarConjuntoAutoArestasEm(int xm, int ym) {
        HashSet conjuntoArestas = new HashSet();
        NoPolar no;
        ArestaPolar aresta;
        ChaveDeAresta ch;
        double x, y, r, rm;
        final int delta = 3;
        for (int i = 0; i<nos.size(); i++) {
            no = (NoPolar)nos.get(i);
            if (!no.eEscondido() ) {
                r = no.raio + ArestaPolar.raioArestaCircular;
                x = PolarCartesiano.polar2Cartesiano_x(r,no.angulo, centro);
                y = PolarCartesiano.polar2Cartesiano_y(r,no.angulo, centro);
                rm = PolarCartesiano.distanciaEntrePontos(x,y,xm,ym);
                if (ArestaPolar.raioArestaCircular-delta <= rm && rm <=ArestaPolar.raioArestaCircular+delta) {
                    ch = new ChaveDeAresta(no.angulo, no.retornarAnel(),no.angulo, no.retornarAnel());
                    aresta = (ArestaPolar)arestasHash.get(ch);
                    if (aresta!=null) {
                        if (!aresta.eEscondida() && aresta.eAtingida()) {
                            conjuntoArestas.add(aresta);
                        }
                    }
                }
            }
        }
        return (conjuntoArestas);
    }
    
    /** Retorna o conjunto de arestas em um ponto M = (xm,ym)
     * @param xm Abscissa do ponto M.
     * @param ym Ordenada do ponto M.
     * @return Conjunto de arestas que passam pelo ponto M.
     */
    public Collection retornarConjuntoArestasEm(int xm, int ym) {
        Collection resposta;
        if (nos.size()<arestas.size()) {
            resposta = retornarConjuntoArestasEmLinear(xm, ym);
        } else {
            resposta = retornarConjuntoArestasEmQuadratico(xm, ym);
        }
        resposta.addAll(retornarConjuntoAutoArestasEm(xm,ym));
        return(resposta);
    }
    
    /** Limpa o grafo, removendo arestas, n�s e grupos.
     */
    public void limparGrafo() {
        super.limparGrafo();
        numeroNosCentrais.setValue(0);
        numeroNosPerifericos.setValue(0);
    }
    
}