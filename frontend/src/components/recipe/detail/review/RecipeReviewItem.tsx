import { useEffect, useState } from 'react'
import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewItem.styled'
import { palette } from '../../../../constants/Styles'
import useIcon from '../../../../hooks/useIcon'
import axiosInstance from '../../../../utils/FetchCall'
import ModalWithTwoButton from '../../../ui/ModalWithTwoButton'
import defaultProfile from '../../../../../public/images/default_profile.png'

interface ReviewItemProps {
  review:
    | {
        recipeId: number
        recipeReviewId: number
        nickname: string
        profileUrl: string
        content: string
        star: number | undefined
        cookUrl: string
        createdAt: string
      }
    | undefined
}

function RecipeReviewItem({ review }: ReviewItemProps) {
  const { StarLight, StarFill } = useIcon()
  const [reviewEditValue, setReviewEditValue] = useState('')
  const [isEditing, setIsEditing] = useState(false)
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)

  const [myNickname, setMyNickname] = useState('')
  const [rating, setRating] = useState(0)

  const getProfile = async () => {
    const res = await axiosInstance.get(`/api/profile/private`)

    if (res.status === 200) {
      const commentData = res
      const fetchedNickname = commentData.data.nickName
      setMyNickname(fetchedNickname)
    }
  }

  const handleRatingChange = (newRating: number) => {
    if (newRating >= 1 && newRating <= 5) {
      setRating(newRating)
    }
  }

  const handleStarClick = (starValue: number) => {
    handleRatingChange(starValue)
  }

  // 수정 확인 버튼 클릭 시
  const handleEditConfirmClick = async () => {
    try {
      const res = await axiosInstance.put(
        // 해당 리뷰의 id, 바꿀 content, 바꿀 star
        `/api/review/recipe/${review?.recipeReviewId}?content=${reviewEditValue}!&star=${rating}`
      )
      if (res.status === 200) {
        console.log('리뷰 수정 성공')
      }
    } catch (error) {
      console.error('리뷰 업데이트 실패', error)
    }
  }

  // 댓글 수정 취소
  const handleCancelEdit = () => {
    setIsEditing(false)
    setReviewEditValue('')
  }

  // 수정 버튼 누르면
  const handleEditComment = (
    reviewIdParams: number,
    modifiedContentParams: string
  ) => {
    setIsEditing(true)
    setReviewEditValue(modifiedContentParams)

    console.log('수정버튼누르면', modifiedContentParams, reviewIdParams)
  }

  // 삭제 버튼 누름
  const handleDeleteComment = () => {
    setIsDeleteModalOpen(true)
  }

  // 삭제 모달 확인 버튼
  const handleConfirmDelete = async () => {
    const res = await axiosInstance.delete(
      `/api/review/recipe/${review?.recipeReviewId}`
    )
    try {
      if (res.status === 200) {
        console.log('리뷰 삭제성공?!')
        setIsDeleteModalOpen(false)
        window.location.reload()
      }
    } catch (error) {
      console.error('리뷰 삭제 안되서 에러남', error)
    } finally {
      console.log('review final')
    }
  }

  useEffect(() => {
    getProfile()
  }, [])

  return (
    <S.ReviewLiBox>
      <S.CookImgWrap>
        <S.CookImg src={review?.cookUrl} alt="요리 이미지" />
      </S.CookImgWrap>
      <S.ReviewCon>
        <S.ReviewTxtWrap>
          <S.ReviewHeader>
            <S.ReviewUserInfo>
              <S.UserImg
                src={review?.profileUrl || defaultProfile}
                alt={`${review?.nickname}`}
              />
              <S.UserNickname>{review?.nickname}</S.UserNickname>
            </S.ReviewUserInfo>
            <S.ReviewStar>
              {[1, 2, 3, 4, 5].map(index =>
                index <= (review?.star || 0) ? (
                  <StarFill key={index} size={2} color={palette.yellow} />
                ) : (
                  <StarLight key={index} size={2} color={palette.divider} />
                )
              )}
            </S.ReviewStar>
          </S.ReviewHeader>

          {isEditing ? (
            <S.ReviewEdit>
              <S.ReviewWriteStar>
                {[1, 2, 3, 4, 5].map(starValue => (
                  <S.ReviewStar
                    key={starValue}
                    onClick={() => handleStarClick(starValue)}
                    role="button"
                    tabIndex={0}
                  >
                    {starValue <= rating ? (
                      <StarFill size={3} color={palette.yellow} />
                    ) : (
                      <StarLight size={3} color={palette.divider} />
                    )}
                  </S.ReviewStar>
                ))}
              </S.ReviewWriteStar>
              <S.ReviewEditInput
                value={reviewEditValue}
                onChange={e => setReviewEditValue(e.target.value)}
              />
              <S.ReviewButtonWrap>
                <CommonButton
                  size="small"
                  color="divider"
                  onClick={handleEditConfirmClick}
                >
                  확인
                </CommonButton>
                <CommonButton
                  size="small"
                  color="divider"
                  onClick={handleCancelEdit}
                >
                  취소
                </CommonButton>
              </S.ReviewButtonWrap>
            </S.ReviewEdit>
          ) : (
            <>
              <S.ReviewBody>
                <S.ReviewContent>{review?.content}</S.ReviewContent>
              </S.ReviewBody>
              <S.ReviewFooter>
                <S.ReviewDate>{review?.createdAt.slice(0, 10)}</S.ReviewDate>
              </S.ReviewFooter>

              {review?.nickname === myNickname ? (
                <S.ReviewButtonWrap>
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={() =>
                      handleEditComment(review?.recipeReviewId, review?.content)
                    }
                  >
                    수정
                  </CommonButton>

                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={() => handleDeleteComment()}
                  >
                    삭제
                  </CommonButton>
                </S.ReviewButtonWrap>
              ) : (
                <S.ReviewButtonWrap />
              )}
            </>
          )}
          {/* 삭제 Modal */}
          {isDeleteModalOpen && (
            <ModalWithTwoButton
              content="정말 삭제하시겠어요?"
              subContent="삭제 버튼 선택 시, 후기 삭제되며 복구되지 않습니다."
              setIsModalFalse={() => setIsDeleteModalOpen(false)}
              modalEvent={handleConfirmDelete}
            />
          )}
        </S.ReviewTxtWrap>
      </S.ReviewCon>
    </S.ReviewLiBox>
  )
}

export default RecipeReviewItem
