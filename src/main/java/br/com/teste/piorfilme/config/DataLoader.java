package br.com.teste.piorfilme.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import br.com.teste.piorfilme.entity.Filme;
import br.com.teste.piorfilme.repositories.FilmeRepository;

//@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private FilmeRepository filmeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verifica se já existem dados no banco
        if (filmeRepository.count() > 0) {
            System.out.println("✅ Banco já possui dados. Carregamento ignorado.");
            return;
        }

        System.out.println("📂 Carregando dados do CSV...");

        // Lê o arquivo CSV da pasta resources
        ClassPathResource resource = new ClassPathResource("arquivo.csv");
        InputStream inputStream = resource.getInputStream();

        // Configura o parser
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.getFormat().setDelimiter(';');

        CsvParser parser = new CsvParser(settings);
        List<Record> records = parser.parseAllRecords(inputStream);

        System.out.println("📊 Total de registros lidos: " + records.size());

        // Converte para entidades
        List<Filme> filmes = new ArrayList<>();
        records.forEach(record -> {
            Filme filme = new Filme();
            filme.setAno(Integer.parseInt(record.getString("ano")));
            filme.setNome(record.getString("nome"));
            filme.setProdutor(record.getString("produtor"));

            String venceuStr = record.getString("venceu");
            filme.setVenceu(venceuStr != null && venceuStr.trim().equals("1"));

            filmes.add(filme);
        });

        // Salva no banco
        filmeRepository.saveAll(filmes);
        
        System.out.println("✅ " + filmes.size() + " filmes carregados com sucesso!");
    }
}