package yubo.v1.lambda.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.Validate.notNull;

public class Artist {
    private String name;

    private List<Artist> members;

    private String nationality;

    public Artist(final String name, final String nationality) {
        this(name, new ArrayList<Artist>(), nationality);
    }

    public Artist(final String name, final List<Artist> members, final String nationality) {
        notNull(name);
        notNull(members);
        notNull(nationality);

        this.name = name;
        this.members = members;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public Stream<Artist> getMembers() {
        return members.stream();
    }

    public String getNationality() {
        return nationality;
    }

    public boolean isSolo() {
        return members.isEmpty();
    }

    public boolean isFrom(final String nationality) {
        return this.nationality.equals(nationality);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("members", members)
                .append("nationality", nationality)
                .toString();
    }

    public Artist copy() {
        final List<Artist> members = getMembers().map(Artist::copy).collect(Collectors.toList());
        return new Artist(name, members, nationality);
    }
}
