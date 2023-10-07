import useIcon from '../hooks/useIcon'
import * as S from '../styles/StarRating.styled'

const StarRating = ({ rating, size }) => {
    const { IcStar, IcEmptyStar } = useIcon()
    const yelloStarCount = rating
    const emptyStarCount = 5 - rating
    return (
        <S.StarContainer>
            {Array.from({ length: yelloStarCount }, () => (
                <IcStar key={1} size={size} />
            ))}
            {Array.from({ length: emptyStarCount }, () => (
                <IcEmptyStar key={1} size={size} />
            ))}
        </S.StarContainer>
    )
}

export default StarRating
