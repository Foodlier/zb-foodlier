import { useState } from 'react'
import { Link } from 'react-router-dom'
import useIcon from '../../../../hooks/useIcon'
import RecipeReviewItem from './RecipeReviewItem'
import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewList.styled'
import { palette } from '../../../../constants/Styles'

function RecipeReviewList() {
  const initialReviews = [
    {
      recipeReviewId: '1',
      nickname: '사용자1',
      content:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
      star: 5,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-07',
    },
    {
      recipeReviewId: '2',
      nickname: '사용자2',
      content: '너무 맛있어요!',
      star: 4,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-08',
    },
    {
      recipeReviewId: '3',
      nickname: '사용자1',
      content:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
      star: 5,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-07',
    },
    {
      recipeReviewId: '4',
      nickname: '사용자2',
      content: '너무 맛있어요!',
      star: 3,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-08',
    },
    {
      recipeReviewId: '5',
      nickname: '사용자1',
      content:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
      star: 2,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-07',
    },
    {
      recipeReviewId: '6',
      nickname: '사용자2',
      content: '너무 맛있어요!',
      star: 1,
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      cookUrl: 'https://source.unsplash.com/random/300x300/?food',
      createdAt: '2023-10-08',
    },
  ]

  const [reviews, setReviews] = useState(initialReviews)
  const [visibleReviews, setVisibleReviews] = useState(5)
  const { IcAddLight } = useIcon()

  const handleShowMore = () => {
    console.log(setReviews)
    setVisibleReviews(visibleReviews + 5)
  }

  return (
    <S.ReviewContainer>
      <S.ReviewtitBox>
        <S.Reviewtit>후기</S.Reviewtit>
        <Link to="/recipe/detail/write-review">
          <CommonButton color="main">
            <IcAddLight size={2} color={palette.white} />
            후기 작성하기
          </CommonButton>
        </Link>
      </S.ReviewtitBox>

      <S.ReviewUlBox>
        {reviews.slice(0, visibleReviews).map(review => (
          <RecipeReviewItem key={review.recipeReviewId} review={review} />
        ))}
      </S.ReviewUlBox>

      <S.MoreButtonBox>
        {reviews.length > 5 && visibleReviews < reviews.length && (
          <CommonButton onClick={handleShowMore} color="main" border="border">
            후기 더보기
          </CommonButton>
        )}
      </S.MoreButtonBox>
    </S.ReviewContainer>
  )
}

export default RecipeReviewList
