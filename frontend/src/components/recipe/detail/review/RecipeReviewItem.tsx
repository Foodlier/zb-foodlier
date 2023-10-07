import React from 'react'
import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewItem.styled'
import { palette } from '../../../../constants/Styles'
import useIcon from '../../../../hooks/useIcon'

interface ReviewItemProps {
  review: {
    recipeReviewId: string
    nickname: string
    content: string
    star: number
    profileUrl: string
    cookUrl: string
    createdAt: string
  }
}

function RecipeReviewItem({ review }: ReviewItemProps) {
  const { StarLight, StarFill } = useIcon()

  return (
    <S.ReviewLiBox>
      <S.CookImgWrap>
        <S.CookImg src={review.cookUrl} alt="요리 이미지" />
      </S.CookImgWrap>
      <S.ReviewCon>
        <S.ReviewTxtWrap>
          <S.ReviewHeader>
            <S.ReviewUserInfo>
              <S.UserImg src={review.profileUrl} alt={`${review.nickname}`} />
              <S.UserNickname>{review.nickname}</S.UserNickname>
            </S.ReviewUserInfo>
            <S.ReviewStar>
              {[1, 2, 3, 4, 5].map(index =>
                index <= review.star ? (
                  <StarFill key={index} size={2} color={palette.yellow} />
                ) : (
                  <StarLight key={index} size={2} color={palette.divider} />
                )
              )}
            </S.ReviewStar>
          </S.ReviewHeader>
          <S.ReviewBody>
            <S.ReviewContent>{review.content}</S.ReviewContent>
          </S.ReviewBody>
          <S.ReviewFooter>
            <S.ReviewDate>{review.createdAt}</S.ReviewDate>
          </S.ReviewFooter>
        </S.ReviewTxtWrap>
        <S.ReviewButtonWrap>
          <CommonButton size="small" color="divider">
            수정
          </CommonButton>
          <CommonButton size="small" color="divider">
            삭제
          </CommonButton>
        </S.ReviewButtonWrap>
      </S.ReviewCon>
    </S.ReviewLiBox>
  )
}

export default RecipeReviewItem
