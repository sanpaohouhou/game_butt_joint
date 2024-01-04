package com.tl.tgGame.address.controller;


import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.common.dto.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Resource
    private AddressService addressService;

    @GetMapping("/my")
    public Response myAddress(@Uid Long uid) throws IOException {
        return Response.success(addressService.get(uid));
    }

}
