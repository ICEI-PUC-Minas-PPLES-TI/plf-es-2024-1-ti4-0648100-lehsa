package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.enums.TipoItem;
import com.gerenciadorlehsa.events.DeletarItemEmTransacoesEvent;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.EnumNaoEncontradoException;
import com.gerenciadorlehsa.repository.ItemRepository;
import com.gerenciadorlehsa.service.interfaces.EventPublisher;
import com.gerenciadorlehsa.service.interfaces.ItemService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDServiceImg;
import com.gerenciadorlehsa.service.interfaces.OperacoesImagemService;
import com.gerenciadorlehsa.util.ConstantesImgUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ITEM_SERVICE;
import static java.lang.String.format;

@Service
@Slf4j(topic = ITEM_SERVICE)
@RequiredArgsConstructor
@Schema(description = "Contém as regras de negócio para os itens do laboratório")
public class ItemServiceImpl implements OperacoesCRUDServiceImg<Item>, ItemService, EventPublisher, OperacoesImagemService {


    private final ItemRepository itemRepository;
    private ApplicationEventPublisher eventPublisher;


    // --------------- CRUD - INICIO ---------------------------------------

    @Override
    public byte[] encontrarImagemPorId(@NotNull UUID id) {
        log.info(">>> encontrarImagemPorId: encontrando imagem por id");
        Item itemImagem = encontrarPorId(id);
        try {
            return getImage(itemImagem.getNomeImg());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Item encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando item por id");
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(format("item não encontrado, id: %s", id)));
    }

    @Override
    public List<Item> listarTodos() {
        log.info(">>> listarTodos: listando todos itens");
        return this.itemRepository.findAll();
    }

    @Override
    @Transactional
    public Item criar (@NotNull Item item, MultipartFile img) {
        log.info(">>> criar: criando item");

        try {
            String nomeImagem = saveImageToStorage(img);
            item.setNomeImg(nomeImagem);
            item.setId(null);
            item = this.itemRepository.save(item);
            log.info(format(">>> criar: item criado, id: %s", item.getId()));
            return item;
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Transactional
    public Item atualizar(Item item, MultipartFile img) {
        log.info(">>> atualizar: atualizando item");
        Item itemExistente = encontrarPorId(item.getId());
        List<String> propriedadesNulas = new ArrayList<>();
        processarImagem(itemExistente, img, propriedadesNulas);
        atualizarPropriedadesNulas(item, itemExistente, propriedadesNulas);
        this.itemRepository.save(itemExistente);
        log.info(format(">>> atualizar: item atualizado, id: %s", item.getId()));
        return itemExistente;
    }



    @Override
    @Transactional
    public void deletar (@NotNull UUID id) {
        log.info(">>> deletar: deletando item");
        Item item = encontrarPorId(id);
        DeletarItemEmTransacoesEvent event = new DeletarItemEmTransacoesEvent (this, item);
        publishEvent (event);
        try {
            deleteImage(event.getItem ().getNomeImg());
            this.itemRepository.deleteById(id);
            log.info(format(">>> deletar: item deletado, id: %s", id));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }


// --------------- CRUD - FIM ---------------------------------------



// --------------- OperacoesImagemService - INICIO -----------------------------

    @Override
    public byte[] getImage(String imageName) throws IOException {
        Path imagePath = Path.of(ConstantesImgUtil.DIRETORIO_IMGS, imageName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            throw new EntidadeNaoEncontradaException("Imagem não encontrada"); // Handle missing images
        }
    }

    @Override
    public String saveImageToStorage(MultipartFile imageFile) throws IOException {
        if (!(imageFile.getContentType().equals("image/jpeg") || imageFile.getContentType().equals("image/png"))) {
            throw new RuntimeException("Arquivo para imagem de item é um tipo não aceito");
        }
        if (imageFile.getSize() > 500 * 1024) {
            throw new RuntimeException("Tamanho do arquivo para imagem de item excede 500 KB");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Path.of(ConstantesImgUtil.DIRETORIO_IMGS);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }


    @Override
    public String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of(ConstantesImgUtil.DIRETORIO_IMGS, imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }

    // Colocar na interface
    private void processarImagem(Item itemExistente, MultipartFile img, List<String> propriedadesNulas) {
        log.info("img é null?" + (img.getContentType()));
        if (img.getContentType() == null) {
            propriedadesNulas.add("nomeImagem");
        } else {
            try {
                deleteImage(itemExistente.getNomeImg());
                String nomeImagem = saveImageToStorage(img);
                itemExistente.setNomeImg(nomeImagem);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }



// --------------- OperacoesImagemService - FIM -----------------------------





// --------------- ItemService - Inicio -----------------------------

    @Override
    public List<Item> encontrarPorTipo (@NotNull String tipo) {
        log.info(">>> encontrarPorTipo: encontrando itens com o tipo especificado");
        tipo = tipo.toUpperCase();
        try {
            TipoItem enumTipo = Enum.valueOf(TipoItem.class, tipo);
            return this.itemRepository.findByTipoItem(enumTipo);
        } catch (IllegalArgumentException e) {
            throw  new EnumNaoEncontradoException(format("não existe o tipo passado: " + tipo));
        }
    }

    @Override
    public List<Item> listarEmprestaveis () {
        log.info(">>> listarEmprestaveis: listando itens que são emprestaveis");
        return itemRepository.findEmprestaveis();
    }

    @Override
    public List<Item> encontrarPorNome (@NotNull String nome) {
        log.info(">>> encontrarPorNome: encontrando itens com o nome especificado");
        return this.itemRepository.findByNome(nome);
    }


    // --------------- ItemService - FIM -----------------------------



    // --------------- EventPublish - INICIO -----------------------------

    @Override
    public ApplicationEventPublisher getEventPublisher () {
        return this.eventPublisher;
    }


    @Override
    public void setApplicationEventPublisher (@org.jetbrains.annotations.NotNull ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


// --------------- EventPublish - FIM -----------------------------


    private void atualizarPropriedadesNulas(Item item, Item itemExistente, List<String> propriedadesNulas) {
        propriedadesNulas.addAll(getNullPropertyNames(item));
        String[] arrayS = new String[propriedadesNulas.size()];
        BeanUtils.copyProperties(item, itemExistente, propriedadesNulas.toArray(arrayS));
    }


    private List<String> getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());

        }

        return emptyNames.stream().toList();
    }


}
