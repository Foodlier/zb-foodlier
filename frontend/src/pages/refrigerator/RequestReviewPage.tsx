import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/RequestReviewPage.styled'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import axiosInstance from '../../utils/FetchCall'
import RejectReviewModal from '../../components/refrigerator/RejectReviewModal'

const RequestReviewPage = () => {
  const { StarFill, StarLight } = useIcon()
  const { requestId, name: chefNickName } = useLocation().state
  const [rating, setRating] = useState(5)
  const [review, setReview] = useState('')
  const [isModal, setIsModal] = useState(false)
  const navigate = useNavigate()

  const handleRatingChange = (newRating: number) => {
    if (newRating >= 1 && newRating <= 5) {
      setRating(newRating)
    }
  }

  const handleStarClick = (starValue: number) => {
    handleRatingChange(starValue)
  }

  const postReview = async () => {
    if (review === '') {
      setIsModal(true)
      setTimeout(() => {
        setIsModal(false)
      }, 2000)
      return
    }
    try {
      const res = await axiosInstance.post(`/api/review/chef/${requestId}`, {
        content: review,
        star: rating,
      })
      if (res.status === 200) {
        console.log(res)
        navigate(-1)
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <>
      <Header />
      <S.Container>
        <S.Title>후기 작성하기</S.Title>
        <S.SubTitle>{chefNickName}님에 대한 후기 작성</S.SubTitle>
        <S.ReviewTextArea
          cols={parseInt('30', 10)}
          rows={parseInt('7', 10)}
          placeholder={`${chefNickName}님의 요리는 어땠나요?`}
          onChange={e => setReview(e.target.value)}
        />
        <S.SubTitle>요리사 평점</S.SubTitle>
        <S.ReviewWriteStar>
          {[1, 2, 3, 4, 5].map(starValue => (
            <S.ReviewStar
              key={starValue}
              onClick={() => handleStarClick(starValue)}
              role="button"
              tabIndex={0}
            >
              {starValue <= rating ? (
                <StarFill size={5} color={palette.yellow} />
              ) : (
                <StarLight size={5} color={palette.divider} />
              )}
            </S.ReviewStar>
          ))}
        </S.ReviewWriteStar>
        <S.ButtonList>
          <S.Button $reject type="button" onClick={() => navigate(-1)}>
            취소하기
          </S.Button>
          <S.Button $reject={false} type="button" onClick={postReview}>
            작성하기
          </S.Button>
        </S.ButtonList>
      </S.Container>
      <BottomNavigation />
      {isModal && <RejectReviewModal setIsModal={setIsModal} />}
    </>
  )
}

export default RequestReviewPage
