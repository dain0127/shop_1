package com.changin.shop.service;

import com.changin.shop.entity.Item;
import com.changin.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(Item item){

    }

}
