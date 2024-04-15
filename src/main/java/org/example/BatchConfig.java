package org.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@EnableBatchProcessing
@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Job imprimiOlaMundo() {
        return jobBuilderFactory
                .get("imprimiOlaMundo")
                .start(imprimeOlaStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
    //Tasllet são usadas para pequenas tarefas, exemplo pre processamento como limpesa de arquivos criação de diretorios
    //etc tasklet é executado repetidamente a té o status de completude
    //Chunks usados para tarefas pesada/ leitura/processamento e escrita;
    // cada chunk possui sua propria transação 
    private Step imprimeOlaStep() {
        return stepBuilderFactory
                .get("imprimeOlaSetp")
                .tasklet(imprimiOlaTasklet(null))
                .build();
    }
    @Bean
    @StepScope
    public  Tasklet imprimiOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
        return (stepContribution, chunkContext) -> {
            System.out.println(String.format("Olá, %s!", nome));
            return RepeatStatus.FINISHED;
        };
    }
}
