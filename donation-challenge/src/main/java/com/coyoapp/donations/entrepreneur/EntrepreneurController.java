package com.coyoapp.donations.entrepreneur;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/entrepreneurs")
@RestController
@RequiredArgsConstructor
public class EntrepreneurController {

    private final EntrepreneurService entrepreneurService;

    @PostMapping
    public ResponseEntity createEntrepreneur(@RequestBody EntrepreneurUpdateRequest request) {
        entrepreneurService.updateEntrepreneur(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "list_all_entrepreneurs/{page}")
    public ResponseEntity<List<EntrepreneurDto>> getAll(@PathVariable int page){
        return ResponseEntity.ok(entrepreneurService.getAll(page));
    }

    @GetMapping("{token}")
    public ResponseEntity getEntrepreneur(@PathVariable(name = "token") String invitationToken) {
        return entrepreneurService.getEntrepreneurByInvitationToken(invitationToken)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "transfer-balance")
    public ResponseEntity<TransferBalanceResponse> transferBalance(@RequestBody TransferBalanceRequest request){
        return ResponseEntity.ok().body(entrepreneurService.transferBalance(request));
    }

    /*
    This method is refactored, since invitationtokens means nothing
    and it is not update or insert any information so it should be get method.
    
    @PostMapping("invitationtokens")
    public ResponseEntity createInvitationToken(@RequestBody InvitationTokenRequest request) {
        return entrepreneurService.getByInvitationToken(request.getInvitationToken())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
     */
}
