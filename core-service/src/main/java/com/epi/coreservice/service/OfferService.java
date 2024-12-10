package com.epi.coreservice.service;

import com.epi.coreservice.client.UserServiceClient;
import com.epi.coreservice.dto.UserDto;
import com.epi.coreservice.enums.OfferStatus;
import com.epi.coreservice.exc.NotFoundException;
import com.epi.coreservice.model.Comment;
import com.epi.coreservice.model.Offer;
import com.epi.coreservice.model.Product;
import com.epi.coreservice.repository.OfferRepository;
import com.epi.coreservice.request.notification.SendNotificationRequest;
import com.epi.coreservice.request.offer.MakeAnOfferRequest;
import com.epi.coreservice.request.offer.OfferUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final CommentService orderService;
    private final ProductService productService;
    private final UserServiceClient userServiceclient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic topic;
    private final ModelMapper modelMapper;

    public Offer makeAnOffer(MakeAnOfferRequest request) throws JsonProcessingException {
        String userId = getUserById(request.getUserId()).getId();
        Product product = productService.getProductById(request.getProductId());
        Offer toSave = Offer.builder()
                .userId(userId)
                .product(product)
                .offeredPrice(request.getOfferedPrice())
                .status(OfferStatus.OPEN).build();
        offerRepository.save(toSave);


        ObjectMapper mapper = new ObjectMapper();
        Object notification = mapper.writeValueAsString(
                SendNotificationRequest.builder()
                        .message("You have received an order for your product.")
                        .userId(request.getUserId())
                        .offerId(toSave.getId())
                        .build()
        );


        kafkaTemplate.send(topic.name(), notification);
        return toSave;
    }

    public Offer getOfferById(String id) {
        return findOfferById(id);
    }

    public List<Offer> getOffersByProductId(String id) {
        Comment advert = orderService.getCommentById(id);
        return offerRepository.getOffersByProductId(advert.getId());
    }

    public List<Offer> getOffersByUserId(String id) {
        String userId = getUserById(id).getId();
        return offerRepository.getOffersByUserId(userId);
    }

    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Offer updateOfferById(OfferUpdateRequest request) {
        Offer toUpdate = findOfferById(request.getId());
        modelMapper.map(request, toUpdate);
        return offerRepository.save(toUpdate);
    }

    public void deleteOfferById(String id) {
        offerRepository.deleteById(id);
    }

    public boolean authorizeCheck(String id, String principal) {
        return getUserById(getOfferById(id).getUserId()).getUsername().equals(principal);
    }

    protected Offer findOfferById(String id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer not found"));
    }
}
