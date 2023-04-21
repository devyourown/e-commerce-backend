package org.example.backend.controller;

import org.example.backend.controller.dto.ProductDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Get MainPage List Test")
    public void testGetMainPageList() throws Exception {
        ArrayList<ProductDTO> list;
        MvcResult result = mvc.perform(get("/list")).andReturn();
        result.getResponse().getContentAsString();
    }

    @Test
    @DisplayName("Get One Product Test")
    public void testGetProduct() throws Exception {
        ProductDTO dto;
        MvcResult result = mvc.perform(get("/list/1")).andReturn();
        result.getResponse().getContentAsString();
    }
}