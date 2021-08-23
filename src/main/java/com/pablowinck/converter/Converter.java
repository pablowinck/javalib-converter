package com.pablowinck.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Biblioteca que faz algumas conversões bacanas, as quais agilizam bastante meu
 * dia a dia. <br>
 * GitHub: github.com/pablowinck<br><br>
 *
 * <b>CONVERSÕES</b><br>
 * <ul>
 * <li><b>toTxt</b></li>
 * grava .TXT
 * <li><b>fromTxt</b></li>
 * lê .TXT
 * <li><b>toDate</b></li>
 * transforma String em Date
 * <li><b>fromDate</b></li>
 * transforma Date em String
 * <li><b>toJson</b></li>
 * transforma object em json
 * <li><b>fromJson</b></li>
 * transforma json em object / list object
 * </ul>
 *
 * @author Pablo Winck Winter
 * @since 2021
 * @version 0.0.1-ALPHA
 *
 */
public class Converter {

    /**
     * Diretório documentos ou documents, independente de S.O. ou usuário logado
     */
    public static String DIRETORIO_DOCUMENTOS = javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

    /**
     * Diretório temporário, independente de S.O.<br>
     * <i>no windows é o diretório %temp%</i>
     */
    public static String DIRETORIO_TEMPORARIO = System.getProperty("java.io.tmpdir");

    // Cores para organizar o terminal
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    // retornos
    private static final String OK = "[" + ANSI_GREEN + " OK " + ANSI_RESET + "]";
    private static final String FAIL = "[" + ANSI_RED + " FAIL " + ANSI_RESET + "]";

    // Lista com formatos mais usados de data e data hora
    private static final List<String> formatos = Arrays.asList(
            //Date
            "dd/MM/yyyy", // 0
            "yyyy-MM-dd", // 1
            "M/d/yyyy", // 2
            "M/d/yy", // 3
            "MM/dd/yy", // 4
            "MM/dd/yyyy", // 5
            "yy/MM/dd", // 6
            "dd-MMM-yy", // 7
            //Date Time || Time
            "dd/MM/yyyy HH:mm", // 8
            "dd/MM/yyyy HH:mm:ss", // 9
            "hh:mm", // 10
            "hh:mm:ss.s", // 11
            "mm:ss", // 12
            "mm:ss.s", // 13
            "dd hh:mm", // 14
            "dd hh:mm:ss.s", // 15
            "dd-mmm-yyyy hh:mm", // 16
            "dd-mmm-yyyy hh:mm:ss.s", // 17
            "yyyy-mm-dd hh:mm", // 18
            "yyyy-mm-dd hh:mm:ss.s", // 19
            "yyyy-MM-dd'T'HH:mm:ss.SSS" // 20
    );

    /**
     * <p>
     * grava .TXT no Diretório que inserir, na pasta que parametrizar, com o
     * nome de arquivo que quiser.<br>
     * <b>DIRETORIO_DOCUMENTOS</b> = Documents de cada máquina, independente de
     * S.O.<br>
     * <b>DIRETORIO_TEMPORARIO</b> = Diretório temporário de cada máquina,
     * independente de S.O.
     * </p>
     *
     * @param DIRETORIO O caminho completo onde será salvo o arquivo txt<br>
     * <i>OBS: Não precisa inserir barra no final, ex: C:\\Program Files</i><br>
     * @param PASTA A pasta onde será salvo o arquivo txt<br>
     * <i>OBS: Não precisa inserir barra no final, ex: Textos</i><br>
     * @param ARQUIVO Nome do arquivo txt<br>
     * <i>OBS: Não precisa por .txt no final, ex: exemplo</i><br>
     * @param TEXTO O que irá dentro do txt, para quebra de linha, usar o \n
     */
    public static void toTxt(String DIRETORIO, String PASTA, String ARQUIVO, String TEXTO) {

        System.out.println("Começando a conversão para .txt ...");

        String path = DIRETORIO + "\\" + PASTA + "\\" + ARQUIVO + ".txt";

        File file = new File(path);
        // se existe, ele permite escritura, para poder editar o documento
        if (file.exists()) {
            file.setWritable(true, true);
        } else if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter arq = new FileWriter(path)) {

            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.printf(TEXTO);
            System.out.println(OK + " Arquivo salvo com sucesso!");

            System.out.println("Mudando permissões...");
            file.setReadOnly(); //permite somente leitura do arquivo         
            System.out.println(OK + " Permissões atualizadas c/ sucesso");

        } catch (IOException ex) {
            System.err.println(FAIL + " Erro ao gravar .txt \n\n");
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * <p>
     * lê o .TXT e coloca todo seu conteúdo em uma String.<br><br>
     * <b>DIRETORIO_DOCUMENTOS</b> = Documents de cada máquina, independente de
     * S.O.<br>
     * <b>DIRETORIO_TEMPORARIO</b> = Diretório temporário de cada máquina,
     * independente de S.O.
     * </p>
     *
     * @param DIRETORIO O caminho completo onde está salvo o arquivo txt<br>
     * <i>OBS: Não precisa inserir barra no final, ex: C:\\Program Files</i><br>
     * @param PASTA A pasta onde está salvo o arquivo txt<br>
     * <i>OBS: Não precisa inserir barra no final, ex: Textos</i><br>
     * @param ARQUIVO Nome do arquivo txt<br>
     * <i>OBS: Não precisa por .txt no final, ex: exemplo</i><br>
     * @return Todo conteúdo do .txt em String<br>
     * Caso não achar o .txt, ou ocorrer qualquer erro, retorna uma string
     * vazia. Da para testar com .isEmpty()
     */
    public static String fromTxt(String DIRETORIO, String PASTA, String ARQUIVO) {

        System.out.println("Lendo .txt ...");

        String path = DIRETORIO + "\\" + PASTA + "\\" + ARQUIVO + ".txt";

        File file = new File(path);
        if (file.exists()) {
            try (FileReader arq = new FileReader(path)) {
                BufferedReader lerArq = new BufferedReader(arq);

                String retorno = lerArq.lines().collect(Collectors.joining()); // lê todas linhas

                System.out.println(OK + " Leitura realizada c/ sucesso!");

                return retorno;

            } catch (IOException ex) {
                System.err.println(FAIL + " Erro na leitura do arquivo\n\n" + ex);
                return "";
            }

        } else {
            System.err.println(FAIL + "Arquivo inexistente");
            return "";
        }
    }

    /**
     * Converte String para Date
     *
     * @param data data em string
     * @param isDateTime passar true se for datetime, caso contrário, passar
     * false
     * @return java.util.Date
     */
    public static Date toDate(String data, boolean isDateTime) {

        System.out.println("Começando conversão de data...");

        Date retorno = new Date();
        int contador = !isDateTime ? -1 : 7;
        boolean teste = true;
        while (teste) {
            try {
                contador++;
                if (formatos.size() >= contador) {
                    retorno = new SimpleDateFormat(formatos.get(contador)).parse(data);

                    System.out.println(OK + " Conversão realizada com sucesso");
                    teste = false;

                } else {
                    System.out.println(FAIL + " Retornando data atual, não consegui converter");
                    teste = false;
                }
            } catch (ParseException ex) {
                teste = true;
            }
        }
        return retorno;
    }

    /**
     * <b>FORMATOS:</b><br>
     * 0 --> dd/MM/yyyy                <br>
     * 1 --> yyyy-MM-dd                <br>
     * 2 --> M/d/yyyy                  <br>
     * 3 --> M/d/yy                    <br>
     * 4 --> MM/dd/yy                  <br>
     * 5 --> MM/dd/yyyy                <br>
     * 6 --> yy/MM/dd                  <br>
     * 7 --> dd-MMM-yy                 <br>
     * 8 --> dd/MM/yyyy HH:mm          <br>
     * 9 --> dd/MM/yyyy HH:mm:ss       <br>
     * 10 -> hh:mm                     <br>
     * 11 -> hh:mm:ss.s                <br>
     * 12 -> mm:ss                     <br>
     * 13 -> mm:ss.s                   <br>
     * 14 -> dd hh:mm                  <br>
     * 15 -> dd hh:mm:ss.s             <br>
     * 16 -> dd-mmm-yyyy hh:mm         <br>
     * 17 -> dd-mmm-yyyy hh:mm:ss.s    <br>
     * 18 -> yyyy-mm-dd hh:mm          <br>
     * 19 -> yyyy-mm-dd hh:mm:ss.s     <br>
     * 20 -> yyyy-MM-dd'T'HH:mm:ss.SSS <br>
     *
     * @param data Date a ser convertido
     * @param formato index do formato
     * @return String da data, perante o formato desejado
     */
    public static String fromDate(Date data, int formato) {
        return new SimpleDateFormat(formatos.get(formato)).format(data);
    }

    /**
     * Converte objetos em json's
     * @param objeto
     * objeto a ser convertido
     * @return 
     * json em formato de String
     */
    public static String toJson(Object objeto) {
        return new Gson().toJson(objeto);
    }

    /**
     * Converte JSON em Object<br><br>
     * <b>exemplo de uso:</b><br>
     * Pessoa pessoa = Converter.fromJson(json, Pessoa.class);
     * @param <T>
     * Object, class
     * @param json
     * Json a ser convertido
     * @param type
     * Tipo a ser convertido, ex: Pessoa.class
     * @return 
     * Object do tipo passado
     */
    public static <T extends Object> T fromJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    /**
     * Converte JSON em Object<br><br>
     * <b>exemplo de uso:</b><br>
     * List&lt;Pessoa&gt; pessoas = Converter.fromJson(json, new TypeToken&lt;ArrayList&lt;Pessoa&gt;&gt;(){});
     * @param <T>
     * ArrayList, List
     * @param json
     * Json a ser convertido, ex: [{"nome":"Pablo","altura":"1.84m"},{"nome":"Pablo","altura":"1.84m"}]
     * @param type
     * Tipo a ser convertido, ex: new TypeToken&lt;ArrayList&lt;Pessoa&gt;&gt;(){}
     * @return 
     * List Object do tipo passado
     */
    public static <T extends Object> List<T> fromJson(String json, TypeToken type) {
        return fromJson(json, type.getType());
    }
}
