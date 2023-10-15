import { useEffect, useRef, useState } from 'react'
import * as S from '../../styles/ui/CustomSelect.styled'
import { CustomSelectData } from '../../constants/Interfaces'

const CustomSelect = ({
  data,
  currentSelectValue,
  setCurrentSelectValue,
}: {
  data: CustomSelectData[]
  currentSelectValue: CustomSelectData
  setCurrentSelectValue: (value: CustomSelectData) => void
}) => {
  const containerRef = useRef<HTMLDivElement | null>(null)
  const [isShow, setIsShow] = useState(false)

  const onChangeSelectValue = (value: CustomSelectData) => {
    setCurrentSelectValue(value)
  }

  // select 밖 영역 클릭 시 option 닫히도록 설정
  const onClickOutSide = (e: MouseEvent) => {
    if (
      containerRef.current &&
      !containerRef.current.contains(e.target as Node)
    ) {
      setIsShow(false)
    }
  }

  useEffect(() => {
    document.addEventListener('click', onClickOutSide)
    return () => {
      document.removeEventListener('click', onClickOutSide)
    }
  }, [])

  return (
    <S.Container ref={containerRef} onClick={() => setIsShow(prev => !prev)}>
      <S.Label>{currentSelectValue.text}</S.Label>
      <S.List $show={isShow}>
        {data.map(dataItem => (
          <S.ListItem
            key={dataItem.text}
            onClick={() => onChangeSelectValue(dataItem)}
          >
            {dataItem.text}
          </S.ListItem>
        ))}
      </S.List>
    </S.Container>
  )
}

export default CustomSelect
