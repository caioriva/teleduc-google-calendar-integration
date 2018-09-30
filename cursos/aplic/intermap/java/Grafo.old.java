/*
 * @(#)Grafo1.java        1.0 02/07/2000
 * 
 * Classe Node: No' do grafo onde:
 *                  fixed: especifica o no' formador
 *                  edgeselected: e' setado quando o no' e' selecionado e a aresta devera' mudar de cor
 *                  msgtodos: marca o no' que devera' ser desenhado em formato oval no caso de um
 *                                      participante que enviou mensagens para todos os participantes do curso
 *                  node_todos: especifica o no' Todos
 *
 * Classe Edge: arestas do grafo;
 *
 */

import java.util.*;
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;


class Node 
{
  double x;
  double y;

  double dx;
  double dy;

  boolean fixed;
  boolean edgeselected;
  boolean msgtodos;
  boolean node_todos;

  String lbl;
}


class Edge 
{
  int from;
  int to;
  double len;
}


class GraphPanel extends Panel implements Runnable, MouseListener, MouseMotionListener 
{
  Grafo graph;
  int nnodes;
  Node nodes[] = new Node[200];

  int nedges;
  Edge edges[] = new Edge[1000];

  Thread relaxer;
  Thread desenhe;
  boolean stress;
  boolean random;

  GraphPanel(Grafo graph) 
  {
    this.graph = graph;
    addMouseListener(this);
  }

  int findNode(String lbl) 
  {
    for (int i = 0 ; i < nnodes ; i++) 
    {
      if (nodes[i].lbl.equals(lbl)) 
      {
        return i;
      }
    }
    return addNode(lbl);
  }

  int searchNode(String lbl)
  {
    for (int i = 0 ; i < nnodes ; i++)
    {
      if (nodes[i].lbl.equals(lbl))
      {
        return i;
      }
    }
    return -1; 
  }


  int addNode(String lbl) 
  {
    Node n = new Node();
    n.x = 10 + 380*Math.random();
    n.y = 10 + 380*Math.random();
    n.lbl = lbl;
    nodes[nnodes] = n;
    return nnodes++;
  }

  void addEdge(String from, String to, int len) 
  {
    Edge e = new Edge();
    e.from = findNode(from);
    e.to = findNode(to);
    e.len = len;
    edges[nedges++] = e;
  }

  public void run() 
  {
    Thread me = Thread.currentThread();
        
    while (relaxer == me) 
    {
      relax();
      if (random && (Math.random() < 0.03)) 
      {
        Node n = nodes[(int)(Math.random() * nnodes)];
        if (!n.fixed) 
        {
          n.x += 100*Math.random() - 50;
          n.y += 100*Math.random() - 50;
        }
        graph.play(graph.getCodeBase(), "audio/drip.au");
      }
      try 
      {
        Thread.sleep(100);
      } 
      catch (InterruptedException e) 
      {
        break;
      }
    }

    if (relaxer == null) 
    { 
      while (desenhe == desenhe) 
      {
        repaint();
        if (random && (Math.random() < 0.03)) 
        {
          Node n = nodes[(int)(Math.random() * nnodes)];
          if (!n.fixed) 
          {
            n.x += 100*Math.random() - 50;
            n.y += 100*Math.random() - 50;
          }
          graph.play(graph.getCodeBase(), "audio/drip.au");
        }
        try 
        {
          Thread.sleep(100);
        } 
        catch (InterruptedException e) 
        {
          break;
        }
      }
    }
  }

  synchronized void relax() 
  {
    for (int i = 0 ; i < nedges ; i++) 
    {
      Edge e = edges[i];
      double vx = nodes[e.to].x - nodes[e.from].x;
      double vy = nodes[e.to].y - nodes[e.from].y;
      double len = Math.sqrt(vx * vx + vy * vy);
      len = (len == 0) ? .0001 : len;
      double f = (edges[i].len - len) / (len * 3);
      double dx = f * vx;
      double dy = f * vy;

      nodes[e.to].dx += dx;
      nodes[e.to].dy += dy;
      nodes[e.from].dx += -dx;
      nodes[e.from].dy += -dy;
    }

    for (int i = 0 ; i < nnodes ; i++) 
    {
      Node n1 = nodes[i];
      double dx = 0;
      double dy = 0;

      for (int j = 0 ; j < nnodes ; j++) 
      {
        if (i == j) 
          continue;
        Node n2 = nodes[j];
        double vx = n1.x - n2.x;
        double vy = n1.y - n2.y;
        double len = vx * vx + vy * vy;
        if (len == 0) 
        {
          dx += Math.random();
          dy += Math.random();
        } 
        else if (len < 100*100) 
        {
          dx += vx / len;
          dy += vy / len;
        }
      }
      double dlen = dx * dx + dy * dy;
      if (dlen > 0) 
      {
        dlen = Math.sqrt(dlen) / 2;
        n1.dx += dx / dlen;
        n1.dy += dy / dlen;
      }
    }

    Dimension d = getSize();
    for (int i = 0 ; i < nnodes ; i++) 
    {
      Node n = nodes[i];
      if (!n.fixed) 
      {
        n.x += Math.max(-5, Math.min(5, n.dx));
        n.y += Math.max(-5, Math.min(5, n.dy));
      }
      if (n.x < 0) 
      {
        n.x = 0;
      } 
      else if (n.x > d.width) 
      {
        n.x = d.width;
      }
      if (n.y < 0) 
      {
        n.y = 0;
      } 
      else if (n.y > d.height) 
      {
        n.y = d.height;
      }
      n.dx /= 2;
      n.dy /= 2;
    }
    repaint();
  }

  Node pick;
  boolean pickfixed;
  Image offscreen;
  Dimension offscreensize;
  Graphics offgraphics;

  final Color fixedColor = Color.cyan;
  final Color selectColor = Color.red;
  final Color edgeColor = Color.black;
  final Color nodeColor = new Color(250, 220, 100);
  final Color stressColor = Color.green;

  public void paintNode(Graphics g, Node n, FontMetrics fm) 
  {
    int x = (int)n.x;
    int y = (int)n.y;
    if (n == pick) 
      g.setColor(selectColor);
    else if (n.fixed) 
      g.setColor(fixedColor);
    else if (n.node_todos) 
      g.setColor(stressColor);
    else 
      g.setColor(nodeColor);
                
//        g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
//        g.setColor(n.node_todos ? stressColor : nodeColor);

    int w = fm.stringWidth(n.lbl) + 10;
    int h = fm.getHeight() + 4;
    if (n.msgtodos) 
    {
      g.fillOval(x - w/2, y - h / 2, w, h);
      g.setColor(Color.black);
      g.drawOval(x - w/2, y - h / 2, w-1, h-1);
      g.drawString(n.lbl, x - (w-10)/2, (y - (h-4)/2) + fm.getAscent());
    } 
    else 
    {
      g.fillRect(x - w/2, y - h / 2, w, h);
      g.setColor(Color.black);
      g.drawRect(x - w/2, y - h / 2, w-1, h-1);
      g.drawString(n.lbl, x - (w-10)/2, (y - (h-4)/2) + fm.getAscent());
    }
  }

  public synchronized void update(Graphics g) 
  {
    Dimension d = getSize();
    if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) 
    {
      offscreen = createImage(d.width, d.height);
      offscreensize = d;
      offgraphics = offscreen.getGraphics();
      offgraphics.setFont(getFont());
    }

    offgraphics.setColor(getBackground());
    offgraphics.fillRect(0, 0, d.width, d.height);
    for (int i = 0 ; i < nedges ; i++) 
    {
      Edge e = edges[i];
      int x1 = (int)nodes[e.from].x;
      int y1 = (int)nodes[e.from].y;
      int x2 = (int)nodes[e.to].x;
      int y2 = (int)nodes[e.to].y;
      int len = (int)Math.abs(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)) - e.len);
      offgraphics.setColor((nodes[e.from].edgeselected || nodes[e.to].edgeselected) ? selectColor : edgeColor);
      offgraphics.drawLine(x1, y1, x2, y2);
      if (stress) 
      {
        String lbl = String.valueOf(len);
        offgraphics.setColor(stressColor);
        offgraphics.drawString(lbl, x1 + (x2-x1)/2, y1 + (y2-y1)/2);
        offgraphics.setColor(edgeColor);
      }
    }

    FontMetrics fm = offgraphics.getFontMetrics();
    for (int i = 0 ; i < nnodes ; i++) 
    {
      paintNode(offgraphics, nodes[i], fm);
    }
    g.drawImage(offscreen, 0, 0, null);
  }

  //1.1 event handling
  public void mouseClicked(MouseEvent e) 
  {
  }

  public void mousePressed(MouseEvent e) 
  {
    addMouseMotionListener(this);
    double bestdist = Double.MAX_VALUE;
    int x = e.getX();
    int y = e.getY();
    for (int i = 0 ; i < nnodes ; i++) 
    {
      Node n = nodes[i];
      double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
      if (dist < bestdist) 
      {
        pick = n;
        bestdist = dist;
      }
    }
    pickfixed = pick.fixed;
    pick.fixed = true;
    pick.edgeselected = true;
    pick.x = x;
    pick.y = y;
    relaxer = null;
    repaint();
    e.consume();
  }

  public void mouseReleased(MouseEvent e) 
  {
    removeMouseMotionListener(this);
    pick.x = e.getX();
    pick.y = e.getY();
    pick.fixed = pickfixed;
    pick.edgeselected = false;
    pick = null;
    relaxer = null;
    repaint();
    e.consume();
  }

  public void mouseEntered(MouseEvent e) 
  {
  }

  public void mouseExited(MouseEvent e) 
  {
  }

  public void mouseDragged(MouseEvent e) 
  {
    pick.x = e.getX();
    pick.y = e.getY();
    relaxer = null;
    repaint();
    e.consume();
  }

  public void mouseMoved(MouseEvent e) 
  {
  }

  public void start() 
  {
    relaxer = new Thread(this);
    desenhe = new Thread(this);
    relaxer.start();
    desenhe.start();
  }

  public void stop() 
  {
    relaxer = null;
  }
}


public class Grafo extends Applet implements ActionListener, ItemListener 
{
  GraphPanel panel;
  Panel controlPanel;

  Button scramble = new Button("Reiniciar");
  Button shake = new Button("Misturar");
  Checkbox stress = new Checkbox("Pesos");
  Checkbox random = new Checkbox("Aleat�rio");

  public void init() 
  {
    setLayout(new BorderLayout());

    panel = new GraphPanel(this);
    add("Center", panel);
    controlPanel = new Panel();
    add("South", controlPanel);

    Dimension d = getSize();

    int max = (Integer.valueOf(getParameter("codigo_maximo"))).intValue();

    String[] nomes = new String[max+1];

    String lista_codigo_nome = getParameter("codigo_nome");

    for (StringTokenizer tkNomes = new StringTokenizer(lista_codigo_nome, "/") ; tkNomes.hasMoreTokens() ; )
    {
      String str = tkNomes.nextToken();
      StringTokenizer tkCodNome = new StringTokenizer(str, ":");
      int codigo=Integer.valueOf(tkCodNome.nextToken()).intValue();
      String nome=tkCodNome.nextToken();

      nomes[codigo]=nome;      
    }


    String edges = getParameter("edges");

    for (StringTokenizer tkEdges = new StringTokenizer(edges, "/") ; tkEdges.hasMoreTokens() ; )
    {
      String str = tkEdges.nextToken();
      // str contem uma lista do tipo: cod_origem:cod_destino,tam.cod_destino,tam....

      StringTokenizer tkEdgesCods = new StringTokenizer(str, ":");
      int cod_origem=Integer.valueOf(tkEdgesCods.nextToken()).intValue();
      String cod_destino_tam=tkEdgesCods.nextToken();
     
      // cod_origem contem agora o c�digo do usuario de origem
      // cod_destino_tam tem uma lista do tipo cod_destino,tam.cod_destino,tam...

      for (StringTokenizer tkEdgesDestino = new StringTokenizer(cod_destino_tam, ".") ; tkEdgesDestino.hasMoreTokens() ; )
      {
        String str1 = tkEdgesDestino.nextToken();
        StringTokenizer tkEdgesCodsDestino = new StringTokenizer(str1, ",");

        int cod_destino=Integer.valueOf(tkEdgesCodsDestino.nextToken()).intValue();
        int valor=Integer.valueOf(tkEdgesCodsDestino.nextToken()).intValue();

        panel.addEdge(nomes[cod_origem], nomes[cod_destino], valor);
      }

    }

    String formadores = getParameter("formador");
    if (formadores != null)
    {
      for (StringTokenizer tkFormador = new StringTokenizer(formadores, "/"); tkFormador.hasMoreTokens(); ) 
      {
        int formador = Integer.valueOf(tkFormador.nextToken()).intValue();
        int num_node=panel.searchNode(nomes[formador]);
        if (num_node>=0)
        {
          Node n = panel.nodes[num_node];
          //n.x = d.width / 2;
          //n.y = d.height / 2;
          n.fixed = true;
        }
      }        
    }

    if (nomes[0] != null)  // Nomes[0] � o todos, se for necess�rio
    {
      Node n = panel.nodes[panel.findNode(nomes[0])];
      //n.x = d.width / 2;
      //n.y = d.height / 2;
      n.node_todos = true;
    }


    /*

    String centro = getParameter("centro");
    if (centro != null)
    {
      for (StringTokenizer tc = new StringTokenizer(centro, ","); tc.hasMoreTokens(); ) 
      {
        String no_central = tc.nextToken();
        Node no = panel.nodes[panel.findNode(no_central)];
        no.x = d.width / 2;
        no.y = d.height / 2;
      }        
    }

    String no_todos = getParameter("no_todos");
    if (no_todos != null)
    {
      for (StringTokenizer tf = new StringTokenizer(no_todos, ","); tf.hasMoreTokens(); ) 
      {
        String node_all = tf.nextToken();
        Node n = panel.nodes[panel.findNode(node_all)];
        //n.x = d.width / 2;
        //n.y = d.height / 2;
        n.node_todos = true;
      }        
    }
        
    String msgparatodos = getParameter("mensagemtodos");
    if (msgparatodos != null)
    {
      for (StringTokenizer tf = new StringTokenizer(msgparatodos, ","); tf.hasMoreTokens(); ) 
      {
        String todos = tf.nextToken();
        Node n = panel.nodes[panel.findNode(todos)];
        n.msgtodos = true;
      }        
    }
    */
  }

  public void destroy() 
  {
    remove(panel);
    remove(controlPanel);
  }

  public void start() 
  {
    panel.start();
  }

  public void stop() 
  {
    panel.stop();
  }

  public void actionPerformed(ActionEvent e) 
  {
    Object src = e.getSource();

    if (src == scramble) 
    {
      // play(getCodeBase(), "audio/computer.au");
      Dimension d = getSize();
      for (int i = 0 ; i < panel.nnodes ; i++) 
      {
        Node n = panel.nodes[i];
        if (!n.fixed) 
        {
          n.x = 10 + (d.width-20)*Math.random();
          n.y = 10 + (d.height-20)*Math.random();
        }
      }
      return;
    }

    if (src == shake) 
    {
      //  play(getCodeBase(), "audio/gong.au");
      Dimension d = getSize();
      for (int i = 0 ; i < panel.nnodes ; i++) 
      {
        Node n = panel.nodes[i];
        if (!n.fixed) 
        {
          n.x += 80*Math.random() - 40;
          n.y += 80*Math.random() - 40;
        }
      }
    }
  }

  public void itemStateChanged(ItemEvent e) 
  {
    Object src = e.getSource();
    boolean on = e.getStateChange() == ItemEvent.SELECTED;
    if (src == stress) 
      panel.stress = on;
    else 
      if (src == random) panel.random = on;
  }

  public String getAppletInfo() 
  {
    return "T�tulo: Grafo \nAutor: Luciana Alvim Santos Romani";
  }

  public String[][] getParameterInfo() 
  {
    String[][] info = 
    {
      {"edges", "delimited string", "Uma lista de todas as arestas separadas por virgulas. Tem o formato: 'C-N1,C-N2,C-N3,C-NX,N1-N2/M12,N2-N3/M23,N3-NX/M3X,...' onde C e' o nome do no' central e NX e' um no' atachado ao no' central. Para as arestas conectando os nos (e nao para o no central) pode-se (opcionalmente) especificar um tamanho MXY que separar um no' do outro."},
      {"centro", "string", "No' central do grafo."},
      {"formador", "string", "O nome dos formadores."},
      {"no_todos", "string", "No Todos os participantes"},
      {"mensagemtodos", "string", "O nome dos participantes que enviaram mensagem para todos."}
    };
    return info;
  }
}
