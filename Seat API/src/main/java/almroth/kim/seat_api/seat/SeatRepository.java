package almroth.kim.seat_api.seat;

import almroth.kim.seat_api.seat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    Optional<Set<Seat>> findAllByBusiness_Name(String name);

    Optional<Set<Seat>> findAllByBusiness_Uuid(UUID uuid);

}
