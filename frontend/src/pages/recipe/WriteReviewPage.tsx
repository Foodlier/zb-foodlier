import { useState, ChangeEvent, useRef } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import * as S from '../../styles/recipe/WriteReviewPage.styled'
import CommonButton from '../../components/ui/Button'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import axiosInstance from '../../utils/FetchCall'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'

function ReviewForm() {
  const id = useParams()
  const navigate = useNavigate()
  const { StarFill, StarLight } = useIcon()
  const [rating, setRating] = useState(5)
  const [memo, setMemo] = useState('')
  const { IcAddRoundDuotone } = useIcon()
  const [reviewImage, setReviewImage] = useState<File | null>(null)
  const [reviewModal, setReviewModal] = useState(false)
  const [reviewModalContent, setReviewModalContent] = useState('')
  const reviewImgRef = useRef<HTMLInputElement | null>(null)

  const handleAttachmentChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setReviewImage(e.target.files[0])
    }
  }

  const handleRatingChange = (newRating: number) => {
    if (newRating >= 1 && newRating <= 5) {
      setRating(newRating)
    }
  }

  const handleMemoChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    setMemo(e.target.value)
  }

  const handleSubmit = async () => {
    if (!reviewImage) {
      alert('이미지를 업로드해 주세요.')
      return
    }

    try {
      const reviewImgData = new FormData()
      reviewImgData.append('cookImage', reviewImage)
      reviewImgData.append('content', memo)
      reviewImgData.append('star', String(rating))

      const res = await axiosInstance.post(
        `/api/review/recipe/${id.id}`,
        reviewImgData
      )

      if (res.status === 200) {
        setRating(5)
        setMemo('')
        setReviewImage(null)
        // console.log('게시글 작성됐나?', reviewModal, reviewModalContent)

        setReviewModalContent('게시글 작성이 완료되었습니다.')
      }
    } catch (error) {
      console.error('review 등록 중 오류:', error)
      setReviewModalContent('게시글 작성에 실패하였습니다.')
    }
    setReviewModal(true)
    setTimeout(() => {
      setReviewModal(false)
      navigate(-1)
    }, 1500)

    // console.log('별점:', rating)
    // console.log('후기 내용:', memo)
    // console.log('첨부 파일:', reviewImage)
  }

  const handleStarClick = (starValue: number) => {
    handleRatingChange(starValue)
  }

  return (
    <>
      <Header />

      <S.ReviewWriteContainer>
        <S.ReviewWriteTit>레시피 후기 작성하기</S.ReviewWriteTit>
        <S.ReviewWriteForm
          onSubmit={handleSubmit}
          encType="multipart/form-data"
        >
          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>사진 업로드</S.ReviewWriteTxt>
            {/* {JSON.stringify(reviewImage)} */}
            {reviewImage ? (
              <S.ReviewImage
                src={reviewImage ? URL.createObjectURL(reviewImage) : ''}
                alt=""
              />
            ) : (
              <>
                <S.ReviewFileLabel htmlFor="reviewImg">
                  <IcAddRoundDuotone size={4} />
                </S.ReviewFileLabel>

                <S.ReviewWriteInput
                  type="file"
                  accept="image/*"
                  id="reviewImg"
                  onChange={handleAttachmentChange}
                  ref={reviewImgRef}
                />
              </>
            )}
          </S.ReviewWriteWrap>

          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>후기 내용</S.ReviewWriteTxt>
            <S.ReviewWriteLabel hidden htmlFor="memo">
              후기 내용
            </S.ReviewWriteLabel>
            <S.ReviewWriteTextarea
              id="memo"
              value={memo}
              onChange={handleMemoChange}
              rows={4}
              placeholder="후기 내용을 입력해 주세요"
            />
          </S.ReviewWriteWrap>

          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>레시피 총점</S.ReviewWriteTxt>
            <S.ReviewWriteLabel hidden htmlFor="rating">
              레시피 총점
            </S.ReviewWriteLabel>
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
          </S.ReviewWriteWrap>
        </S.ReviewWriteForm>
        <S.MoreButtonBox>
          <CommonButton
            color="divider"
            size="large"
            onClick={() => navigate(-1)}
          >
            취소하기
          </CommonButton>
          <CommonButton color="main" size="large" onClick={handleSubmit}>
            후기 등록하기
          </CommonButton>
        </S.MoreButtonBox>
      </S.ReviewWriteContainer>
      {reviewModal && (
        <ModalWithoutButton
          content={reviewModalContent}
          setIsModalFalse={() => setReviewModal(false)}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default ReviewForm
