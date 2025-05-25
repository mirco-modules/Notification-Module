package org.khasanof.notification.web.rest;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.notification.IntegrationTest;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.domain.enumeration.Status;
import org.khasanof.notification.repository.tenancy.MessageRepository;
import org.khasanof.notification.service.dto.MessageDTO;
import org.khasanof.notification.service.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TO = "AAAAAAAAAA";
    private static final String UPDATED_TO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_META_DATA = "AAAAAAAAAA";
    private static final String UPDATED_META_DATA = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.SUCCESS;
    private static final Status UPDATED_STATUS = Status.FAILED;

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    private Message message;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message()
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .from(DEFAULT_FROM)
            .to(DEFAULT_TO)
            .deleted(DEFAULT_DELETED)
            .date(DEFAULT_DATE)
            .metaData(DEFAULT_META_DATA)
            .status(DEFAULT_STATUS);
        return message;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity(EntityManager em) {
        Message message = new Message()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .deleted(UPDATED_DELETED)
            .date(UPDATED_DATE)
            .metaData(UPDATED_META_DATA)
            .status(UPDATED_STATUS);
        return message;
    }

    @BeforeEach
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();
        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        restMessageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMessage.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testMessage.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testMessage.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testMessage.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testMessage.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMessage.getMetaData()).isEqualTo(DEFAULT_META_DATA);
        assertThat(testMessage.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId(1L);
        MessageDTO messageDTO = messageMapper.toDto(message);

        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setStatus(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].metaData").value(hasItem(DEFAULT_META_DATA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.metaData").value(DEFAULT_META_DATA))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getMessagesByIdFiltering() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        Long id = message.getId();

        defaultMessageShouldBeFound("id.equals=" + id);
        defaultMessageShouldNotBeFound("id.notEquals=" + id);

        defaultMessageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMessageShouldNotBeFound("id.greaterThan=" + id);

        defaultMessageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMessageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where title equals to DEFAULT_TITLE
        defaultMessageShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the messageList where title equals to UPDATED_TITLE
        defaultMessageShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMessagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMessageShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the messageList where title equals to UPDATED_TITLE
        defaultMessageShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMessagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where title is not null
        defaultMessageShouldBeFound("title.specified=true");

        // Get all the messageList where title is null
        defaultMessageShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where title contains DEFAULT_TITLE
        defaultMessageShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the messageList where title contains UPDATED_TITLE
        defaultMessageShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMessagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where title does not contain DEFAULT_TITLE
        defaultMessageShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the messageList where title does not contain UPDATED_TITLE
        defaultMessageShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMessagesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where body equals to DEFAULT_BODY
        defaultMessageShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the messageList where body equals to UPDATED_BODY
        defaultMessageShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllMessagesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where body in DEFAULT_BODY or UPDATED_BODY
        defaultMessageShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the messageList where body equals to UPDATED_BODY
        defaultMessageShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllMessagesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where body is not null
        defaultMessageShouldBeFound("body.specified=true");

        // Get all the messageList where body is null
        defaultMessageShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByBodyContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where body contains DEFAULT_BODY
        defaultMessageShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the messageList where body contains UPDATED_BODY
        defaultMessageShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllMessagesByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where body does not contain DEFAULT_BODY
        defaultMessageShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the messageList where body does not contain UPDATED_BODY
        defaultMessageShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllMessagesByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where from equals to DEFAULT_FROM
        defaultMessageShouldBeFound("from.equals=" + DEFAULT_FROM);

        // Get all the messageList where from equals to UPDATED_FROM
        defaultMessageShouldNotBeFound("from.equals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    void getAllMessagesByFromIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where from in DEFAULT_FROM or UPDATED_FROM
        defaultMessageShouldBeFound("from.in=" + DEFAULT_FROM + "," + UPDATED_FROM);

        // Get all the messageList where from equals to UPDATED_FROM
        defaultMessageShouldNotBeFound("from.in=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    void getAllMessagesByFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where from is not null
        defaultMessageShouldBeFound("from.specified=true");

        // Get all the messageList where from is null
        defaultMessageShouldNotBeFound("from.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByFromContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where from contains DEFAULT_FROM
        defaultMessageShouldBeFound("from.contains=" + DEFAULT_FROM);

        // Get all the messageList where from contains UPDATED_FROM
        defaultMessageShouldNotBeFound("from.contains=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    void getAllMessagesByFromNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where from does not contain DEFAULT_FROM
        defaultMessageShouldNotBeFound("from.doesNotContain=" + DEFAULT_FROM);

        // Get all the messageList where from does not contain UPDATED_FROM
        defaultMessageShouldBeFound("from.doesNotContain=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    void getAllMessagesByToIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where to equals to DEFAULT_TO
        defaultMessageShouldBeFound("to.equals=" + DEFAULT_TO);

        // Get all the messageList where to equals to UPDATED_TO
        defaultMessageShouldNotBeFound("to.equals=" + UPDATED_TO);
    }

    @Test
    @Transactional
    void getAllMessagesByToIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where to in DEFAULT_TO or UPDATED_TO
        defaultMessageShouldBeFound("to.in=" + DEFAULT_TO + "," + UPDATED_TO);

        // Get all the messageList where to equals to UPDATED_TO
        defaultMessageShouldNotBeFound("to.in=" + UPDATED_TO);
    }

    @Test
    @Transactional
    void getAllMessagesByToIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where to is not null
        defaultMessageShouldBeFound("to.specified=true");

        // Get all the messageList where to is null
        defaultMessageShouldNotBeFound("to.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByToContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where to contains DEFAULT_TO
        defaultMessageShouldBeFound("to.contains=" + DEFAULT_TO);

        // Get all the messageList where to contains UPDATED_TO
        defaultMessageShouldNotBeFound("to.contains=" + UPDATED_TO);
    }

    @Test
    @Transactional
    void getAllMessagesByToNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where to does not contain DEFAULT_TO
        defaultMessageShouldNotBeFound("to.doesNotContain=" + DEFAULT_TO);

        // Get all the messageList where to does not contain UPDATED_TO
        defaultMessageShouldBeFound("to.doesNotContain=" + UPDATED_TO);
    }

    @Test
    @Transactional
    void getAllMessagesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where deleted equals to DEFAULT_DELETED
        defaultMessageShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the messageList where deleted equals to UPDATED_DELETED
        defaultMessageShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllMessagesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultMessageShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the messageList where deleted equals to UPDATED_DELETED
        defaultMessageShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllMessagesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where deleted is not null
        defaultMessageShouldBeFound("deleted.specified=true");

        // Get all the messageList where deleted is null
        defaultMessageShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where date equals to DEFAULT_DATE
        defaultMessageShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the messageList where date equals to UPDATED_DATE
        defaultMessageShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMessagesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMessageShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the messageList where date equals to UPDATED_DATE
        defaultMessageShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMessagesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where date is not null
        defaultMessageShouldBeFound("date.specified=true");

        // Get all the messageList where date is null
        defaultMessageShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByMetaDataIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where metaData equals to DEFAULT_META_DATA
        defaultMessageShouldBeFound("metaData.equals=" + DEFAULT_META_DATA);

        // Get all the messageList where metaData equals to UPDATED_META_DATA
        defaultMessageShouldNotBeFound("metaData.equals=" + UPDATED_META_DATA);
    }

    @Test
    @Transactional
    void getAllMessagesByMetaDataIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where metaData in DEFAULT_META_DATA or UPDATED_META_DATA
        defaultMessageShouldBeFound("metaData.in=" + DEFAULT_META_DATA + "," + UPDATED_META_DATA);

        // Get all the messageList where metaData equals to UPDATED_META_DATA
        defaultMessageShouldNotBeFound("metaData.in=" + UPDATED_META_DATA);
    }

    @Test
    @Transactional
    void getAllMessagesByMetaDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where metaData is not null
        defaultMessageShouldBeFound("metaData.specified=true");

        // Get all the messageList where metaData is null
        defaultMessageShouldNotBeFound("metaData.specified=false");
    }

    @Test
    @Transactional
    void getAllMessagesByMetaDataContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where metaData contains DEFAULT_META_DATA
        defaultMessageShouldBeFound("metaData.contains=" + DEFAULT_META_DATA);

        // Get all the messageList where metaData contains UPDATED_META_DATA
        defaultMessageShouldNotBeFound("metaData.contains=" + UPDATED_META_DATA);
    }

    @Test
    @Transactional
    void getAllMessagesByMetaDataNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where metaData does not contain DEFAULT_META_DATA
        defaultMessageShouldNotBeFound("metaData.doesNotContain=" + DEFAULT_META_DATA);

        // Get all the messageList where metaData does not contain UPDATED_META_DATA
        defaultMessageShouldBeFound("metaData.doesNotContain=" + UPDATED_META_DATA);
    }

    @Test
    @Transactional
    void getAllMessagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where status equals to DEFAULT_STATUS
        defaultMessageShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the messageList where status equals to UPDATED_STATUS
        defaultMessageShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMessagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMessageShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the messageList where status equals to UPDATED_STATUS
        defaultMessageShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMessagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where status is not null
        defaultMessageShouldBeFound("status.specified=true");

        // Get all the messageList where status is null
        defaultMessageShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageShouldBeFound(String filter) throws Exception {
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].metaData").value(hasItem(DEFAULT_META_DATA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageShouldNotBeFound(String filter) throws Exception {
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .deleted(UPDATED_DELETED)
            .date(UPDATED_DATE)
            .metaData(UPDATED_META_DATA)
            .status(UPDATED_STATUS);
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMessage.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testMessage.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testMessage.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testMessage.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testMessage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMessage.getMetaData()).isEqualTo(UPDATED_META_DATA);
        assertThat(testMessage.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage.deleted(UPDATED_DELETED).date(UPDATED_DATE);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMessage.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testMessage.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testMessage.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testMessage.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testMessage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMessage.getMetaData()).isEqualTo(DEFAULT_META_DATA);
        assertThat(testMessage.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .deleted(UPDATED_DELETED)
            .date(UPDATED_DATE)
            .metaData(UPDATED_META_DATA)
            .status(UPDATED_STATUS);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMessage.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testMessage.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testMessage.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testMessage.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testMessage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMessage.getMetaData()).isEqualTo(UPDATED_META_DATA);
        assertThat(testMessage.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(1L);

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, message.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
