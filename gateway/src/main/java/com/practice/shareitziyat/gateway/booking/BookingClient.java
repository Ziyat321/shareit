package com.practice.shareitziyat.gateway.booking;

import com.practice.shareitziyat.gateway.config.BaseClient;
import com.practice.shareitziyat.server.booking.BookingState;
import com.practice.shareitziyat.server.booking.dto.BookingCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
//                        .requestFactory()
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingCreateDto bookingCreate, long userId) {
        return post("", userId, bookingCreate);
    }

    public ResponseEntity<Object> update(int bookingId, boolean approved, long userId) {
        return patch("/{bookingId}?approved={approved}", userId,
                Map.of("bookingId", Integer.toString(bookingId), "approved", approved), null);
    }

    public ResponseEntity<Object> findById(int bookingId, long userId) {
        return get("/{bookingId}", userId, Map.of("bookingId", Integer.toString(bookingId)));
    }

    public ResponseEntity<Object> findAllByBooker(BookingState state, long userId, int from, int size) {
        Map<String, Object> params = Map.of(
                "state", state.toString(),
                "from", Integer.toString(from),
                "size", Integer.toString(size)
        );
        return get("?state={state}&from={from}&size={size}", userId, params);
    }

    public ResponseEntity<Object> findAllByOwner(BookingState state, long ownerId, int from, int size) {
        Map<String, Object> params = Map.of(
                "state", state.toString(),
                "from", Integer.toString(from),
                "size", Integer.toString(size)
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, params);
    }
}
