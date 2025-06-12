package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.RoomTestDto;
import com.capgemini.hospital_management_system.dto.testDto;
import com.capgemini.hospital_management_system.model.Block;
import com.capgemini.hospital_management_system.model.Room;
import com.capgemini.hospital_management_system.repository.BlockRepository;
import com.capgemini.hospital_management_system.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController

@RequestMapping("/test")
public class TestController {

    @Autowired
    RoomRepository roomRepository;

    @GetMapping
    public String m1223(){
        return "I am batman12234";
    }

    @PostMapping()
    public ResponseEntity<?> m123(@RequestBody RoomTestDto dto){
       Room room = new Room();
       room.setRoomNumber(dto.getRoomNumber());
       room.setRoomType(dto.getRoomType());
       Block block = new Block();
       block.setBlockFloor(dto.getTest().getBlockFloor());
       block.setBlockCode(dto.getTest().getBlockCode());
       room.setBlock(block);
       room.setUnavailable(dto.getUnavailable());
       roomRepository.save(room);
       return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
