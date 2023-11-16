import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/user/UserReviewedItem.styled'
import StarRating from '../StarRating'
import axiosInstance from '../../utils/FetchCall'
import useIcon from '../../hooks/useIcon'

interface ReviewdItem {
  recipeId: number
  nickname: string
  star: number
  content: string
  cookUrl: string
  createdAt: string
  profileUrl: string
}

interface UserRecipeItemProps {
  EA: number
  memberId: number | undefined
}

const UserReviewedItem: React.FC<UserRecipeItemProps> = ({ EA, memberId }) => {
  const { InitialUserImg } = useIcon()
  const [reviewedItem, setReviewedItem] = useState<ReviewdItem[]>([])
  const navigate = useNavigate()
  // 받은 레시피 리뷰 가져오기
  const getRecipefReview = async () => {
    try {
      const chefReviewRes = await axiosInstance.get(
        `/api/profile/public/recipe-review/${0}/${EA}/${memberId}`
      )
      setReviewedItem(chefReviewRes.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  const goToReciepe = (id: number) => {
    navigate(`/recipe/detail/${id}`)
  }

  useEffect(() => {
    getRecipefReview()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return reviewedItem.length > 0 ? (
    reviewedItem.map(el => (
      <S.ReviewedCard
        key={el.recipeId}
        onClick={() => goToReciepe(el.recipeId)}
      >
        <S.ReviewedImg src={el.cookUrl} alt="메인 이미지" />
        <S.ReviewedInfoContainer>
          <S.ReviewedTopInfo>
            <S.ReviewedUserInfo>
              {el.profileUrl ? (
                <S.ReviewedUserImg src={el.profileUrl} alt="" />
              ) : (
                <InitialUserImg size={3} />
              )}
              <S.ReviewedUserName>{el.nickname}</S.ReviewedUserName>
            </S.ReviewedUserInfo>
            <StarRating size={1.6} rating={el.star} />
          </S.ReviewedTopInfo>
          <div>
            <S.ReviewedContent>{el.content}</S.ReviewedContent>
            <S.ReviewedAt>{el.createdAt}</S.ReviewedAt>
          </div>
        </S.ReviewedInfoContainer>
      </S.ReviewedCard>
    ))
  ) : (
    <S.NoReviewedCard>받은 후기가 없습니다.</S.NoReviewedCard>
  )
}

export default UserReviewedItem
