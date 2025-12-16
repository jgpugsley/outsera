package br.com.teste.piorfilme.controller;

import java.io.InputStream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


// Configura o contexto Spring Boot completo para o teste
@SpringBootTest
// Configura e injeta o MockMvc para simular requisições HTTP
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Usado para simular chamadas HTTP

    // Teste 1: Garante o carregamento dos dados via POST
    @Test
    @Order(1)
    void testUploadAndDataLoad() throws Exception {
    	// 1. Carrega o arquivo de teste dos recursos
        // A variável 'resource' é criada aqui
        ClassPathResource resource = new ClassPathResource("test-movielist.csv"); 
        
        // 2. LÊ O ARQUIVO COMPLETAMENTE E CONVERTE PARA byte[]
        byte[] fileContent;
        
        // O 'inputStream' é criado a partir de 'resource'
        try (InputStream inputStream = resource.getInputStream()) {
            fileContent = inputStream.readAllBytes(); // Usa o método nativo do Java 9+
            // Se estiver em Java 8, você precisará usar IOUtils.toByteArray(inputStream) do Apache Commons IO
        }
     // 1. Defina o Content Type como String
        String contentType = MediaType.TEXT_PLAIN.toString(); // "text/plain"
     // 2. Cria o MockMultipartFile usando a String
        MockMultipartFile csvFile = new MockMultipartFile(
            "file",             // name: O nome do parâmetro (@RequestParam("file"))
            "movielist.csv", // originalFilename
            contentType,        // contentType: Agora é uma String
            fileContent         // content: O array de bytes
        );

        // 4. Executa a requisição POST de upload
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(csvFile))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Teste 2: Garante a precisão dos resultados (min/max)
    @Test
    @Order(2)
    void testMinMaxIntervalsCalculation() throws Exception {
    	// 1. GARANTE O UPLOAD (Necessário para ter dados no H2)
        // Se o @SpringBootTest estiver configurado para limpar o banco entre testes, 
        // você precisará chamar o upload novamente ou usar um Transactional Test.
        
        // 2. Executa a requisição GET para o endpoint de cálculo
        mockMvc.perform(MockMvcRequestBuilders.get("/produtores/intervalos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                
                // 3. VALIDAÇÃO DOS DADOS (JSON Path Assertions)
                // Valida o Menor Intervalo (Min = 1 ano)
                .andExpect(MockMvcResultMatchers.jsonPath("$.min.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min[0].producer").value("Producer 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min[0].interval").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min[1].producer").value("Producer 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min[1].interval").value(1))

                // Valida o Maior Intervalo (Max = 20 anos)
                .andExpect(MockMvcResultMatchers.jsonPath("$.max.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max[0].producer").value("Producer Long"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max[0].interval").value(20))
     
                .andExpect(MockMvcResultMatchers.jsonPath("$.min[0].interval").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max[0].interval").value(20));
       
        System.out.println("SUCESSO: MIN/MAX passaram!");
    }
}