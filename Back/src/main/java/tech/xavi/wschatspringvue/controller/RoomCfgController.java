package tech.xavi.wschatspringvue.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.model.Avatar;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/cfg")
public class RoomCfgController {

    @GetMapping("users-limit")
    public ResponseEntity<?> getUsersLimit(){
        return new ResponseEntity<>(Globals.MAX_ROOM_USERS, HttpStatus.OK);
    }

    @GetMapping("avatar-styles")
    public ResponseEntity<?> getAvailableAvatars(){
        return new ResponseEntity<>(Avatar.availableAvatarsUrls(),HttpStatus.OK);
    }

}
