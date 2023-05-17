package subway.business.domain.direction;

import java.util.List;
import subway.business.domain.Section;
import subway.business.domain.Station;

public class UpwardLineModifyStrategy implements LineModifyStrategy {

    @Override
    public void addTerminus(Station station, List<Section> sections, int distance) {
        Section terminalSection = sections.get(0);
        Station terminus = terminalSection.getUpwardStation();
        sections.add(0, Section.createToSave(station, terminus, distance));
    }

    @Override
    public void addMiddleStation(Station station, Station neighborhoodStation, List<Section> sections, int distance) {
        Section sectionToRemove = sections.stream()
                .filter(section -> section.getDownwardStation().hasNameOf(neighborhoodStation.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 이웃 역입니다. "
                        + "(입력한 이웃 역 : %s)", neighborhoodStation.getName())));
        Section sectionToAddUpward = Section.createToSave(
                sectionToRemove.getUpwardStation(),
                station,
                sectionToRemove.calculateRemainingDistance(distance)
        );
        Section sectionToAddDownward = Section.createToSave(
                station,
                sectionToRemove.getDownwardStation(),
                distance
        );
        int indexToAdd = sections.indexOf(sectionToRemove);
        sections.remove(sectionToRemove);
        sections.add(indexToAdd, sectionToAddDownward);
        sections.add(indexToAdd, sectionToAddUpward);
    }

    @Override
    public Station getTerminus(List<Section> sections) {
        return sections.get(0).getUpwardStation();
    }
}

