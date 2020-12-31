
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Random;

public class Main {

    private static int tam;
    private static Vertice adj[];
    private static Vertice arvoreGM[];
    private static Aresta aresta;
    private static ArrayList<Aresta> arestas = new ArrayList();
    private static int tempo;
    private static int d[];
    private static int t[];
    private static String cor[];
    private static int antecessor[];
    private static boolean fimListaAdj;
    private static Vertice aux;
    private static boolean flagCiclico;
    private static Vertice dij[];
    private static Vertice lista[];
    private static boolean flag[];
    private static int pesos[];

    private static int dist[];

    static void criar(int tam) {
        adj = new Vertice[tam];
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new Vertice();
            adj[i].v = -1;
            adj[i].p = -1;
            adj[i].prox = null;

        }
        System.out.println("Grafo criado com sucesso");
    }

    static boolean existeAresta(int u, int v) {
        Vertice aux = adj[u];
        while (aux.prox != null) {
            aux = aux.prox;
            if (aux.v == v) {
                return (true);
            }
        }
        return (false);
    }

    static boolean inserirAresta(int u, int v, int p) {
        Vertice aux = new Vertice();
        Vertice temp = adj[u];
        Vertice temp2 = temp.prox;
        aux.v = v;
        aux.p = p;
        while (temp2 != null) {
            if (temp2.v > aux.v) {
                temp.prox = new Vertice();
                temp.prox = aux;
                temp = temp.prox;
                temp.prox = temp2;
                return (true);
            } else if (temp2.v == aux.v) {
                return (false);
            }
            temp = temp.prox;
            temp2 = temp2.prox;
        }
        temp.prox = new Vertice();
        temp.prox = aux;
        temp = temp.prox;
        temp.prox = null;
        return (true);
    }

    static boolean removeAresta(int u, int v) {

        Vertice temp = adj[u];
        Vertice aux = temp.prox;
        while (aux != null) {
            if (aux.v == v) {
                temp.prox = aux.prox;
                aux = null;
                return (true);
            }
            temp = temp.prox;
            aux = temp.prox;
        }
        return (false);

    }

    static void destruirGrafo() {
        for (int i = 0; i < adj.length; i++) {
            Vertice temp = adj[i];
            Vertice aux = temp.prox;

            while (temp.prox != null) {
                temp.prox = aux.prox;
                aux = null;
                aux = temp.prox;
            }
            aux = adj[i];
            aux = null;
        }

    }

    static void imprimirGrafo() {
        for (int i = 0; i < adj.length; i++) {
            Vertice aux = adj[i];
            System.out.print("|" + i + "|-->");
            while (aux.prox != null) {
                aux = aux.prox;
                System.out.print("|" + aux.v + "|" + aux.p + "|-->");
            }
            System.out.println("NULL");
        }
    }

    static boolean existeAdjacente(int u) {

        aux = adj[u];
        if (aux.prox == null) {
            return (false);
        } else {
            return (true);
        }
    }

    static Vertice primeiroAdjacente(int u) {
        Vertice aux = adj[u];
        return aux.prox;
    }

    static Vertice proximoAdjacente(int u, int v) {
        Vertice aux = adj[u];
        while (aux.prox != null) {
            aux = aux.prox;
            if (aux.v == v) {
                break;
            }
        }
        return aux.prox;

    }

    static int proxAdjacente(int u, int v) {

        aux = adj[u];
        while (aux.prox != null) {
            aux = aux.prox;
            if (aux.v == v) {
                break;
            }
        }
        if (aux.prox == null) {
            fimListaAdj = true;
            return (-1);

        } else {
            return (aux.prox.v);

        }
    }

    static void visitaDfs(int u) {
        int v;
        cor[u] = "c";// vertice já foi visitado
        tempo++;//cronometro aumenta
        d[u] = tempo;// o vetor de cinza na pos do vertice recebe a marcação do cronômetro
        System.out.println("Visita o vertice: " + u + " Tempo Descoberta: " + d[u] + "  cinza.");
        if (existeAdjacente(u)) {// verifica se este vertice possui um adjacente

            v = primeiroAdjacente(u).v;//aux recebe o primeiro adjacente
            fimListaAdj = false;//se a lista acabou ou não
            while (!fimListaAdj) {

                if ("b".equals(cor[v])) {

                    antecessor[u] = v;
                    visitaDfs(v);
                }

                v = proxAdjacente(u, v);
            }
        }
        cor[u] = "p";
        tempo++;
        t[u] = tempo;
        System.out.println("Visita ao Vertice: " + u + " Tempo termino: " + t[u] + " preto.");

    }

    static void visitaDfsCiclico(int u, int op) {
        int v;
        cor[u] = "c";// vertice já foi visitado
        tempo++;//cronometro aumenta
        d[u] = tempo;// o vetor de cinza na pos do vertice recebe a marcação do cronômetro

        if (existeAdjacente(u)) {// verifica se este vertice possui um adjacente

            v = primeiroAdjacente(u).v;//aux recebe o primeiro adjacente
            fimListaAdj = false;//se a lista acabou ou não
            while (!fimListaAdj) {

                if ("b".equals(cor[v])) {

                    antecessor[u] = v;
                    visitaDfsCiclico(v, op);
                } else if ("c".equals(cor[v]) && op == 1) {
                    flagCiclico = true;
                }
                v = proxAdjacente(u, v);

            }
        }
        cor[u] = "p";
        tempo++;
        t[u] = tempo;

    }

    static void buscaEmProfundidade(int tam, int op) {
        tempo = 0;//cronometro
        d = new int[tam];//marca quando o vertice fica cinza
        t = new int[tam];//marca quando o vertice fica preto
        cor = new String[tam];//cor do vertice
        antecessor = new int[tam];//armazena o adjacente anterior de cada vertice
        for (int u = 0; u < tam; u++) {//deixa todos os vertices brancos e o antecessor como -1
            cor[u] = "b";
            antecessor[u] = -1;
        }
        if (op == 0) {
            for (int u = 0; u < tam; u++) {//inicia a busca, pelo vertice 0
                if ("b".equals(cor[u])) {
                    visitaDfs(u);
                }
            }

        } else {
            flagCiclico = false;
            for (int u = 0; u < tam; u++) {//inicia a busca, pelo vertice 0
                if ("b".equals(cor[u])) {
                    visitaDfsCiclico(u, op);

                }
            }
            if (flagCiclico == true) {
                System.out.println("O Vertice é ciclico.");
            } else {
                System.out.println("O Vertice não é cíclico.");
            }
        }
    }
    //
    //ex008
    //

    static void visitaBfs(int u) {
        int cont = 0, primeiro = u;
        int v;
        int item;
        Queue<Integer> fila = new LinkedList();

        cor[u] = "c";
        dist[u] = 0;
        fila.clear();
        item = u;
        fila.add(item);
        System.out.println("--Iniciada visita \n\tOrigem: " + u + "\n\tCor: cinza\n\tDistância: " + dist[u]);
        imprimirFila(fila);

        while (!fila.isEmpty()) {
            item = fila.remove();
            u = item;
            System.out.println("--Vizinhos de " + u);
            if (existeAdjacente(u)) {
                v = primeiroAdjacente(u).v;
                fimListaAdj = false;
                while (fimListaAdj == false) {
                    if ("b".equals(cor[v])) {
                        cont++;
                        cor[v] = "c";
                        dist[v] = dist[u] + 1;
                        antecessor[v] = u;
                        item = v;
                        System.out.println("--" + cont + "º Vizinho: " + v + "\n\tCor: cinza\n\tDistância: " + dist[v]);
                        fila.add(item);

                    }
                    v = proxAdjacente(u, v);
                }

            }
            cor[u] = "p";
            if (cont == 0) {
                System.out.println("\tNão possui vizinhos brancos.");
            }
            System.out.println("--Fim dos Vizinhos \n\tVertice " + u + "\n\tCor: preto \n\tDistancia: " + dist[u]);
            cont = 0;
        }
        System.out.println("--Fim do Laço \n\tOrigem em " + primeiro);
        imprimirFila(fila);
    }

    static void buscaEmLargura(int tam) {
        dist = new int[tam];
        cor = new String[tam];
        antecessor = new int[tam];
        for (int x = 0; x < tam; x++) {
            cor[x] = "b";
            dist[x] = -1;
            antecessor[x] = -1;
        }
        for (int x = 0; x < tam; x++) {
            if ("b".equals(cor[x])) {
                visitaBfs(x);
            }
        }
    }

    static void imprimirFila(Queue<Integer> fila) {
        Iterator it = fila.iterator();
        System.out.println("Conteúdo da fila: ");
        while (it.hasNext()) {
            System.out.print(it.next() + ", ");
        }
        System.out.println("");
    }

    // Trabalho
    public static void preencherLista() {
        for (int i = 0; i < adj.length; i++) {
            Vertice aux = adj[i];
            while (aux.prox != null) {
                aux = aux.prox;
                aresta = new Aresta();
                aresta.u = i;
                aresta.v = aux.v;
                aresta.p = aux.p;
                arestas.add(aresta);
            }
        }
        Collections.sort(arestas);
    }

    public static void listarArestas() {
        for (Aresta a : arestas) {
            System.out.print("U: " + a.u);
            System.out.print(" V: " + a.v);
            System.out.println(" P " + a.p);
        }
    }

    static void criarArvore(int tam) {
        arvoreGM = new Vertice[tam];
        for (int i = 0; i < arvoreGM.length; i++) {
            arvoreGM[i] = new Vertice();
            arvoreGM[i].v = -1;
            arvoreGM[i].p = -1;
            arvoreGM[i].x = adj[i].x;
            arvoreGM[i].y = adj[i].y;
            arvoreGM[i].prox = null;
        }
        System.out.println("Grafo criado com sucesso");
    }

    static boolean inserirNaArvore(int u, int v, int p) {
        Vertice aux = new Vertice();
        Vertice temp = arvoreGM[u];
        Vertice temp2 = temp.prox;
        aux.v = v;
        aux.p = p;
        while (temp2 != null) {
            if (temp2.v > aux.v) {
                temp.prox = new Vertice();
                temp.prox = aux;
                temp = temp.prox;
                temp.prox = temp2;
                return (true);
            } else if (temp2.v == aux.v) {
                return (false);
            }
            temp = temp.prox;
            temp2 = temp2.prox;
        }
        temp.prox = new Vertice();
        temp.prox = aux;
        temp = temp.prox;
        temp.prox = null;
        return (true);
    }

    static void imprimirArvore() {
        for (int i = 0; i < arvoreGM.length; i++) {
            Vertice aux = arvoreGM[i];
            System.out.print("|" + i + "|-->");
            while (aux.prox != null) {
                aux = aux.prox;
                System.out.print("|" + aux.v + "|" + aux.p + "|-->");
            }
            System.out.println("NULL");
        }
    }

    public static void kruskal(int tam) {
        criarArvore(tam);
        int conjuntos[] = new int[tam];
        for (int i = 0; i < tam; i++) {
            conjuntos[i] = i;
        }
        preencherLista();
        for (Aresta a : arestas) {
            //System.out.println(conjuntos[a.u] + " " + conjuntos[a.v]);
            if (conjuntos[a.u] != conjuntos[a.v]) {
                inserirNaArvore(a.u, a.v, a.p);
                for (int i = 0; i < conjuntos.length; i++) {
                    if (conjuntos[i] == conjuntos[a.u] || conjuntos[i] == conjuntos[a.v]) {
                        conjuntos[i] = a.u;
                    }
                }
            }
        }
        imprimirArvore();
    }

    public static void teste() {
        inserirAresta(0, 1, 1);
        inserirAresta(0, 3, 3);
        inserirAresta(0, 4, 10);
        inserirAresta(1, 2, 5);
        inserirAresta(2, 4, 1);
        inserirAresta(3, 2, 2);
        inserirAresta(3, 4, 6);
    }

    static void criarMenor(int tam) {
        dij = new Vertice[tam];
        for (int i = 0; i < dij.length; i++) {
            dij[i] = new Vertice();
            dij[i].v = -1;
            dij[i].p = -1;
            dij[i].x = adj[i].x;
            dij[i].y = adj[i].y;
            dij[i].prox = null;
        }
        System.out.println("Grafo criado com sucesso");
    }

    static int contTrue() {
        //retorna quantidade de trues dentro do vetor
        int cont = 0;
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == true) {
                cont += 1;
            }
        }
        return cont;

    }

    static int pegarMenor() {
        //pega o menor peso do índice(ordena)
        Vertice menor = new Vertice();
        menor.p = 100000;
        int pos = 0;
        for (int i = 0; i < lista.length; i++) {
            if (menor.p > lista[i].p && flag[i] == true) {
                menor = lista[i];
                pos = i;
            }
        }
        return pos;
    }

    static void mosArvGerMin(int pi, int pf) {
        criarMenor(tam);
        
        int temp=pf;
        while(temp!=pi){
            inserirArestaArvoreGM(antecessor[temp], temp, pesos[temp]);
            temp = antecessor[temp];
        }
        imprimirHtml(dij);
    }

     static boolean inserirArestaArvoreGM(int u, int v, int p) {
        Vertice aux = new Vertice();
        Vertice temp = dij[u];
        Vertice temp2 = temp.prox;
        aux.v = v;
        aux.p = p;
        while (temp2 != null) {
            if (temp2.v > aux.v) {
                temp.prox = new Vertice();
                temp.prox = aux;
                temp = temp.prox;
                temp.prox = temp2;
                return (true);
            } else if (temp2.v == aux.v) {
                return (false);
            }
            temp = temp.prox;
            temp2 = temp2.prox;
        }
        temp.prox = new Vertice();
        temp.prox = aux;
        temp = temp.prox;
        temp.prox = null;
        return (true);
    }
    
    static void dijkstra(int pi, int pf, int tam) {
        lista = new Vertice[tam];
        flag = new boolean[tam];
        antecessor = new int[tam];
        pesos = new int[tam];
        int cont;
        Vertice aux1;
        for (int x = 0; x < tam; x++) {
            aux1 = new Vertice();
            aux1.v = x;
            aux1.p = 10000;
            lista[x] = aux1;
            flag[x] = true;
            antecessor[x] = -1;
        }
        lista[pi].p = 0;
        aux1 = new Vertice();
        cont = contTrue();
        while (cont >= 1) {
            aux1 = lista[pegarMenor()];
            flag[pegarMenor()] = false;

            if (existeAdjacente(aux1.v)) {
                Vertice aux2 = primeiroAdjacente(aux1.v);
                while (aux2 != null) {
                    if (lista[aux2.v].p > aux1.p + aux2.p) {
                        antecessor[aux2.v] = aux1.v;
                        lista[aux2.v].p = aux1.p + aux2.p;
                        pesos[aux2.v] = aux2.p;
                    }
                    aux2 = proximoAdjacente(aux1.v, aux2.v);
                }

            }
            cont = contTrue();
        }
       mosArvGerMin(pi, pf);

    }

    static void imprimirHtml(Vertice adj[]) {
        String vertices = "", arestas = "", modelo;
        FileOutputStream fo;
        int na = 0;
        Vertice aux;
        try {
            modelo = new String(Files.readAllBytes(Paths.get("modelo.html")), StandardCharsets.UTF_8);
            for (int i = 0; i < adj.length; i++) {
                if (i != 0) {
                    vertices += ",";
                }
                vertices += "{\"l\":" + i + ", \"x\": "+adj[i].x+", \"y\": "+adj[i].y+"}";
            }
            for (int i = 0; i < adj.length; i++) {
                aux = adj[i];
                while (aux.prox != null) {
                    aux = aux.prox;
                    if (na != 0) {
                        arestas += ",";
                    }
                    arestas += "{\"o\": " + i + ", \"d\": " + aux.v + "}";
                    na++;
                }

            }
            modelo = modelo.replace("{v}", vertices);
            modelo = modelo.replace("{a}", arestas);
            fo = new FileOutputStream(new File("index.html"));
            fo.write(modelo.getBytes());
            fo.close();
            Runtime.getRuntime().exec("C:\\Program Files\\Mozilla Firefox\\firefox.exe index.html");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void lerAqr() throws IOException {
        Scanner leitura;
        int u, v, arestas;
        int p;
        File file = new File("E:/Documentos/Netbeans Projects/Grafos_Ponteiros Atualizacao 06-12/grafosRosinei.txt");
        leitura = new Scanner(file);
        tam = leitura.nextInt();
        criar(tam);
        for (int i = 0; i < tam; i++) {
            arestas = leitura.nextInt();
            adj[i].x = leitura.nextInt();
            adj[i].y = leitura.nextInt();
        }
        
        arestas = leitura.nextInt();
        while (leitura.hasNext()) {
            u = leitura.nextInt();
            v = leitura.nextInt();
            p = leitura.nextInt();
            inserirAresta(u, v, p);
        }
    }

    public static void main(String[] args) throws IOException {
        int op, u, v, p;
        Random gerador = new Random();
        Scanner s = new Scanner(System.in);
        do {
            System.out.println("\t\t\tMENU:");
            System.out.println("0 - Sair");
            System.out.println("1 - Criar vazio;");
            System.out.println("2 - Existe aresta;");
            System.out.println("3 - Inserir aresta;");
            System.out.println("4 - Remover aresta;");
            System.out.println("5 - Destruir;");
            System.out.println("6 - Imprimir Vertice;");
            System.out.println("7 - Imprimir HTML;");
            System.out.println("8 - Existe adjacente;");
            System.out.println("9 - Primeiro adjacente;");
            System.out.println("10 - Proximo adjacente;");
            System.out.println("11 - Pesquisa em profundidade;");
            System.out.println("12 - Verifica  se é ciclico;");
            System.out.println("13 - Criar e preencher  o Vertice aleatoriamente: ");
            System.out.println("14 - Busca em largura;");
            System.out.println("15 - Preencher lista;");
            System.out.println("16 - Listar arestas;");
            System.out.println("17 - Lançar o kruskal;");
            System.out.println("18 - Lançar o dijkstra;");
            System.out.println("19 - Imprimir html;");
            System.out.println("Digite sua opção: ");
            op = s.nextInt();
            switch (op) {
                case 0:
                    System.out.println("Saindo...");
                    break;
                case 1:
                    /*System.out.println("Entre com o tamanho: ");
                    tam = s.nextInt();
                    criar(tam);
                    teste();*/
                    lerAqr();
                    break;
                case 2:
                    System.out.println("Entre com o primeiro vertice: ");
                    u = s.nextInt();
                    System.out.println("Entre com o segundo vertice: ");
                    v = s.nextInt();

                    if (existeAresta(u, v)) {
                        System.out.println("Existe aresta.");
                    } else {
                        System.out.println("Não existe aresta.");
                    }

                    break;
                case 3:
                    System.out.println("Entre com o primeiro vertice: ");
                    u = s.nextInt();
                    System.out.println("Entre com o segundo vertice: ");
                    v = s.nextInt();
                    System.out.println("Entre com o modulo: ");
                    p = s.nextInt();
                    if (inserirAresta(u, v, p)) {
                        System.out.println("A aresta foi inserida com sucesso.");
                    } else {
                        System.out.println("A aresta já existe.1");
                    }
                    break;
                case 4:
                    System.out.println("Entre com o número do primeiro vertice: ");
                    u = s.nextInt();
                    System.out.println("Entre com o número do segundo vertice: ");
                    v = s.nextInt();
                    if (removeAresta(u, v)) {
                        System.out.println("Aresta removida com sucesso.");
                    } else {
                        System.out.println("Tal aresta não existe!");
                    }

                    break;
                case 5:
                    destruirGrafo();
                    System.out.println("Grafo Destruido.");

                    break;
                case 6:
                    imprimirGrafo();
                    break;
                case 7:
                    System.out.println("Indisponível no momento.");
                    break;
                case 8:

                    System.out.println("Digite o número do vertice: ");
                    u = s.nextInt();
                    if (!existeAdjacente(u)) {
                        System.out.println("Não existe adjacente.");
                    } else {
                        System.out.println("Existe adjacente.");
                    }
                    break;

                case 9:
                    System.out.println("Digite o número do vertice: ");
                    u = s.nextInt();
                    System.out.println("Primeiro adjacente: " + primeiroAdjacente(u));
                    break;
                case 10:
                    System.out.println("Entre com o vertice: ");
                    u = s.nextInt();
                    System.out.println("Entre com o ultimo: ");
                    v = s.nextInt();
                    System.out.println("O proximo adjacente é o vertice: " + proxAdjacente(u, v));
                    break;
                case 11:
                    buscaEmProfundidade(tam, 0);
                    break;
                case 12:

                    buscaEmProfundidade(tam, 1);
                    break;
                case 13:
                    tam = gerador.nextInt(10);
                    if (tam < 5) {
                        tam += 4;
                    }
                    criar(tam);
                    for (int i = 0; i < tam; i++) {
                        inserirAresta(gerador.nextInt(tam), gerador.nextInt(tam), gerador.nextInt(tam));
                    }
                    imprimirGrafo();
                    break;
                case 14:
                    buscaEmLargura(tam);
                    break;
      
                case 17:
                    kruskal(tam);
                    imprimirHtml(arvoreGM);
                    break;
                case 18:
                    int pi,
                     pf;
                    System.out.println("Entre com o ponto inicial e o ponto final:");
                    pi = s.nextInt();
                    pf = s.nextInt();

                    dijkstra(pi, pf, tam);
                    break;
                case 19:
                    imprimirHtml(adj);
                    break;
                default:
                    System.out.println("Opção Inválida.");
            }
        } while (op != 0);

    }
}
